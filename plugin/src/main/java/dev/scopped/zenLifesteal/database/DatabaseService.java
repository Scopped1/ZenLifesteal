package dev.scopped.zenLifesteal.database;

import dev.scopped.zenLifesteal.LifestealPlugin;
import dev.scopped.zenLifesteal.database.connection.ConnectionPoolManager;
import dev.scopped.zenLifesteal.user.model.LifestealUserImpl;
import dev.scopped.zenLifesteal.user.models.LifestealUser;
import dev.scopped.zenLifesteal.user.models.statistics.StatisticType;
import dev.scopped.zenLifesteal.leaderboard.model.LeaderboardEntry;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService implements DatabaseQuery {

    private final LifestealPlugin plugin;
    private final ConnectionPoolManager poolManager;

    public DatabaseService(LifestealPlugin plugin) {
        this.plugin = plugin;
        this.poolManager = new ConnectionPoolManager(plugin);

        update(CREATE_USER_TABLE);
        update(CREATE_USER_STATISTICS_TABLE);
    }

    public LifestealUser getOrCreate(UUID uuid, String name) {
        try (CachedRowSet cachedRowSet = query(GET_USER_BY_UUID, uuid.toString())) {
            if (cachedRowSet.next()) {
                int id = cachedRowSet.getInt("id");

                return loadUser(uuid, name, id);
            } else return createUser(name, uuid);
        } catch (SQLException e) {
            plugin.loggerProvider().error("Failed to get user by UUID", e);
        }

        return null;
    }

    public LifestealUser loadByName(String name) {
        try (CachedRowSet cachedRowSet = query(GET_USER_BY_NAME, name)) {
            if (cachedRowSet.next()) {
                int id = cachedRowSet.getInt("id");
                UUID uuid = UUID.fromString(cachedRowSet.getString("uuid"));

                return loadUser(uuid, name, id);
            }
        } catch (SQLException e) {
            plugin.loggerProvider().error("Failed to load user by name", e);
        }

        return null;
    }

    private LifestealUser createUser(String name, UUID uuid) {
        try (Connection connection = connection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement insertUser = connection.prepareStatement(INSERT_USER, PreparedStatement.RETURN_GENERATED_KEYS);
                 PreparedStatement insertStatistics = connection.prepareStatement(INSERT_USER_STATISTICS)) {
                insertUser.setString(1, uuid.toString());
                insertUser.setString(2, name);

                if (insertUser.executeUpdate() > 0) {
                    try (ResultSet resultSet = insertUser.getGeneratedKeys()) {
                        if (resultSet.next()) {
                            int id = resultSet.getInt(1);

                            insertStatistics.setInt(1, id);

                            if (insertStatistics.executeUpdate() > 0) {
                                connection.commit();

                                return loadUser(uuid, name, id);
                            }
                        }
                    }
                }

                throw new SQLException("Failed to create user");
            } catch (SQLException e) {
                connection.rollback();
                plugin.loggerProvider().error("Failed to create user. Transaction rolled back.", e);
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            plugin.loggerProvider().error("Failed to create user", e);
        }

        return null;
    }

    private LifestealUser loadUser(UUID uuid, String name, int id) {
        LifestealUser user = new LifestealUserImpl(id, uuid, name);

        try (CachedRowSet statistics = query(GET_USER_STATISTICS, id)) {
            if (!statistics.next()) {
                throw new SQLException("Failed to load user statistics");
            }

            for (StatisticType statistic : StatisticType.VALUES) {
                user.statistics().set(statistic, statistics.getLong(statistic.id()));
            }
        } catch (SQLException e) {
            plugin.loggerProvider().error("Failed to load user data", e);
        }

        return user;
    }

    public synchronized boolean statisticSet(int userId, StatisticType statistic, long amount) {
        return update(UPDATE_STATISTICS.formatted(statistic.id()), amount, userId);
    }

    public synchronized boolean statisticAdd(int userId, StatisticType statistic, long amount) {
        return update(ADD_STATISTIC.formatted(statistic.id(), statistic.id()), amount, userId);
    }

    public boolean statisticRemove(int userId, StatisticType statistic, long amount) {
        return update(REMOVE_STATISTIC.formatted(statistic.id(), statistic.id()), amount, userId);
    }

    public List<LeaderboardEntry> leaderboard(StatisticType type, int limit) {
        List<LeaderboardEntry> result = new ArrayList<>();

        try (CachedRowSet rows = query(GET_LEADERBOARD.formatted(type.id()), limit)) {
            if (rows == null) return result;

            while (rows.next()) {
                String name = rows.getString("name");
                long amount = rows.getLong("amount");

                result.add(new LeaderboardEntry(name, amount));
            }
        } catch (SQLException e) {
            plugin.loggerProvider().error("Failed to load leaderboard for " + type.name(), e);
        }

        return result;
    }

    public int userId(String name) {
        try (CachedRowSet cachedRowSet = query(GET_USER_BY_NAME, name)) {
            if (cachedRowSet.next()) {
                return cachedRowSet.getInt("id");
            }
        } catch (SQLException e) {
            plugin.loggerProvider().error("Failed to get user ID", e);
        }

        return -1;
    }

    public CachedRowSet query(String query, Object... objects) {
        try (Connection connection = connection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            for (int i = 0; i < objects.length; i++) {
                if (objects[i].getClass().isArray()) {
                    for (Object object : (Object[]) objects[i]) {
                        preparedStatement.setObject(i + 1, object);
                    }
                } else {
                    preparedStatement.setObject(i + 1, objects[i]);
                }
            }

            CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
            cachedRowSet.populate(preparedStatement.executeQuery());

            return cachedRowSet;
        } catch (SQLException e) {
            plugin.loggerProvider().error("Failed to execute query", e);
            return null;
        }
    }

    public boolean update(String query, Object... objects) {
        try (Connection connection = connection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            for (int i = 0; i < objects.length; i++)
                preparedStatement.setObject(i + 1, objects[i]);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            plugin.loggerProvider().error("Failed to execute update", e);
            return false;
        }
    }

    public Connection connection() throws SQLException {
        return poolManager.connection();
    }

    public void closePool() {
        poolManager.closePool();
    }

}

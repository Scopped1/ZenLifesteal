package dev.scopped.zenLifesteal.database.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.scopped.zenLifesteal.LifestealPlugin;
import dev.scopped.zenLifesteal.config.SettingsConfig;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPoolManager {

    private final LifestealPlugin plugin;

    private HikariDataSource dataSource;

    public ConnectionPoolManager(LifestealPlugin plugin) {
        this.plugin = plugin;

        setupPool();
    }

    private void setupPool() {
        HikariConfig config = new HikariConfig();

        SettingsConfig settingsConfig = plugin.settingsConfig();

        String hostname = settingsConfig.mysqlHostname;
        int port = settingsConfig.mysqlPort;
        String database = settingsConfig.mysqlDatabase;
        String username = settingsConfig.mysqlUsername;
        String password = settingsConfig.mysqlPassword;

        config.setJdbcUrl("jdbc:mysql://" + hostname + ":" + port + "/" + database);
        config.setUsername(username);
        config.setPassword(password);

        config.addDataSourceProperty("cachePrepStmts", true);
        config.addDataSourceProperty("prepStmtCacheSize", 250);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        config.addDataSourceProperty("useUnicode", true);
        config.addDataSourceProperty("useSSL", false);

        config.setPoolName("smp-crystals-pool");

        dataSource = new HikariDataSource(config);
    }

    public Connection connection() throws SQLException {
        return dataSource.getConnection();
    }

    public void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

}

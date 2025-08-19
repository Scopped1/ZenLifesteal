package dev.scopped.zenLifesteal.user.struct;

import dev.scopped.zenLifesteal.LifestealPlugin;
import dev.scopped.zenLifesteal.user.models.LifestealUser;
import dev.scopped.zenLifesteal.user.models.statistics.StatisticType;
import it.ytnoos.loadit.api.DataContainer;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class UserServiceImpl implements UserService {

    private final LifestealPlugin plugin;
    private final DataContainer<LifestealUser> container;

    public UserServiceImpl(LifestealPlugin plugin) {
        this.plugin = plugin;
        this.container = plugin.container();
    }

    @Override
    public CompletableFuture<Optional<LifestealUser>> offlineUser(String name) {
        Player player = plugin.server().getPlayerExact(name);

        if (player != null) {
            return CompletableFuture.completedFuture(Optional.of(container.getCached(player)));
        }

        return container.get(name);
    }

    @Override
    public LifestealUser onlineUser(Player player) {
        return container.getCached(player);
    }

    @Override
    public Optional<LifestealUser> onlineUser(String name) {
        Player player = plugin.server().getPlayerExact(name);

        if (player != null) return Optional.of(container.getCached(player));

        return Optional.empty();
    }

    @Override
    public CompletableFuture<Boolean> setStatistic(String name, StatisticType type, long amount) {
        return plugin.async(() -> {
            int userId = id(name);
            if (userId == -1) return false;

            if (plugin.databaseService().statisticSet(userId, type, amount)) {
                onlineUser(name).ifPresent(user -> user.statistics().set(type, amount));
                return true;
            }

            return false;
        });
    }

    @Override
    public CompletableFuture<Boolean> addStatistic(String name, StatisticType type, long amount) {
        return plugin.async(() -> {
            int userId = id(name);
            if (userId == -1) return false;

            if (plugin.databaseService().statisticAdd(userId, type, amount)) {
                onlineUser(name).ifPresent(user -> user.statistics().add(type, amount));
                return true;
            }

            return false;
        });
    }

    @Override
    public CompletableFuture<Boolean> removeStatistic(String name, StatisticType type, long amount) {
        return plugin.async(() -> {
            int userId = id(name);
            if (userId == -1) return false;

            if (plugin.databaseService().statisticRemove(userId, type, amount)) {
                onlineUser(name).ifPresent(user -> user.statistics().remove(type, amount));
                return true;
            }

            return false;
        });
    }

    @Override
    public int id(String name) {
        return onlineUser(name).isPresent() ? onlineUser(name).get().id() : plugin.databaseService().userId(name);
    }
}

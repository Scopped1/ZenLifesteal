package dev.scopped.zenLifesteal.user.struct;

import dev.scopped.zenLifesteal.user.models.LifestealUser;
import dev.scopped.zenLifesteal.user.models.statistics.StatisticType;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface UserService {

    CompletableFuture<Optional<LifestealUser>> offlineUser(String name);

    LifestealUser onlineUser(Player player);

    Optional<LifestealUser> onlineUser(String name);

    CompletableFuture<Boolean> setStatistic(String name, StatisticType type, long amount);

    CompletableFuture<Boolean> addStatistic(String name, StatisticType type, long amount);

    CompletableFuture<Boolean> removeStatistic(String name, StatisticType type, long amount);

    int id(String name);

}

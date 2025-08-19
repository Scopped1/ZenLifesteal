package dev.scopped.zenLifesteal;

import dev.scopped.zenLifesteal.combat.CombatLogService;
import dev.scopped.zenLifesteal.config.MessagesConfig;
import dev.scopped.zenLifesteal.config.SettingsConfig;
import dev.scopped.zenLifesteal.leaderboard.LeaderboardService;
import dev.scopped.zenLifesteal.providers.LoggerProvider;
import dev.scopped.zenLifesteal.providers.MessageProvider;
import dev.scopped.zenLifesteal.user.models.LifestealUser;
import dev.scopped.zenLifesteal.user.struct.UserService;
import it.ytnoos.loadit.api.DataContainer;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

public interface LifestealAPI {

    void reload();

    void disable();

    Server server();

    LoggerProvider loggerProvider();

    MessageProvider messageProvider();

    DataContainer<LifestealUser> container();

    UserService userService();

    LeaderboardService leaderboardService();

    CombatLogService combatLogService();

    SettingsConfig settingsConfig();

    MessagesConfig messagesConfig();

    ExecutorService executor();

    <T> CompletableFuture<T> async(Supplier<T> supplier);

    JavaPlugin bootstrap();

}

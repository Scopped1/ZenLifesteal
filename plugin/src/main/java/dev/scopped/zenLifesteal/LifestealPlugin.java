package dev.scopped.zenLifesteal;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import dev.scopped.zenLifesteal.combat.CombatLogService;
import dev.scopped.zenLifesteal.combat.CombatLogServiceImpl;
import dev.scopped.zenLifesteal.config.MessagesConfig;
import dev.scopped.zenLifesteal.config.SettingsConfig;
import dev.scopped.zenLifesteal.database.DatabaseService;
import dev.scopped.zenLifesteal.hooks.papi.PlaceholderHook;
import dev.scopped.zenLifesteal.leaderboard.LeaderboardService;
import dev.scopped.zenLifesteal.leaderboard.LeaderboardServiceImpl;
import dev.scopped.zenLifesteal.listeners.CombatListeners;
import dev.scopped.zenLifesteal.listeners.PlayerListeners;
import dev.scopped.zenLifesteal.providers.LoggerProvider;
import dev.scopped.zenLifesteal.providers.LoggerProviderImpl;
import dev.scopped.zenLifesteal.providers.MessageProvider;
import dev.scopped.zenLifesteal.providers.MessageProviderImpl;
import dev.scopped.zenLifesteal.tasks.CombatTrackerTask;
import dev.scopped.zenLifesteal.user.loader.LifestealDataLoader;
import dev.scopped.zenLifesteal.user.models.LifestealUser;
import dev.scopped.zenLifesteal.user.models.statistics.StatisticType;
import dev.scopped.zenLifesteal.user.struct.UserService;
import dev.scopped.zenLifesteal.user.struct.UserServiceImpl;
import it.ytnoos.loadit.Loadit;
import it.ytnoos.loadit.api.DataContainer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class LifestealPlugin implements LifestealAPI {

    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(6);
    private final LifestealBootstrap bootstrap;

    private final PaperCommandManager commandManager;

    private final SettingsConfig settingsConfig;
    private final MessagesConfig messagesConfig;

    private final LoggerProvider loggerProvider;
    private final MessageProvider messageProvider;

    private final DatabaseService databaseService;
    private final Loadit<LifestealUser> loadit;
    private final DataContainer<LifestealUser> container;

    private final UserService userService;
    private final LeaderboardService leaderboardService;
    private final CombatLogService combatLogService;

    public LifestealPlugin(LifestealBootstrap bootstrap) {
        this.bootstrap = bootstrap;

        this.commandManager = new PaperCommandManager(bootstrap);

        commandManager.getCommandCompletions().registerAsyncCompletion("players",
                context -> server().getOnlinePlayers().stream().map(Player::getName).toList());

        commandManager.getCommandCompletions().registerAsyncCompletion("statistics", context ->
                Arrays.stream(StatisticType.VALUES)
                        .filter(type -> type != StatisticType.LAST_JOIN && type != StatisticType.FIRST_JOIN
                                && type != StatisticType.PLAYTIME && type != StatisticType.HEARTS)
                        .map(StatisticType::name)
                        .toList()
        );

        this.settingsConfig = new SettingsConfig(this);
        this.messagesConfig = new MessagesConfig(this);

        this.loggerProvider = new LoggerProviderImpl();
        this.messageProvider = new MessageProviderImpl();

        this.databaseService = new DatabaseService(this);

        this.loadit = Loadit.createInstance(bootstrap, new LifestealDataLoader(this));
        loadit.init();

        this.container = loadit.getContainer();

        this.userService = new UserServiceImpl(this);
        this.leaderboardService = new LeaderboardServiceImpl(this);
        leaderboardService.register();
        this.combatLogService = new CombatLogServiceImpl(this);

        server().getServicesManager().register(LifestealAPI.class, this, bootstrap, ServicePriority.Normal);

        listeners(
                new PlayerListeners(this),
                new CombatListeners(this)
        );

        new PlaceholderHook(this).register();
        new CombatTrackerTask(this).register();
    }

    private void commands(BaseCommand... commands) {
        for (BaseCommand command : commands) {
            commandManager.registerCommand(command);
        }
    }

    private void listeners(Listener... listeners) {
        for (Listener listener : listeners) {
            server().getPluginManager().registerEvents(listener, bootstrap);
        }
    }

    @Override
    public void reload() {
        settingsConfig.reload();
        messagesConfig.reload();
    }

    @Override
    public void disable() {
        combatLogService.flush();
        loadit.stop();
        databaseService.closePool();
    }

    @Override
    public Server server() {
        return bootstrap.getServer();
    }

    @Override
    public LoggerProvider loggerProvider() {
        return loggerProvider;
    }

    @Override
    public MessageProvider messageProvider() {
        return messageProvider;
    }

    @Override
    public DataContainer<LifestealUser> container() {
        return container;
    }

    @Override
    public UserService userService() {
        return userService;
    }

    @Override
    public CombatLogService combatLogService() {
        return combatLogService;
    }

    @Override
    public SettingsConfig settingsConfig() {
        return settingsConfig;
    }

    @Override
    public MessagesConfig messagesConfig() {
        return messagesConfig;
    }

    public DatabaseService databaseService() {
        return databaseService;
    }

    @Override
    public ExecutorService executor() {
        return EXECUTOR;
    }

    @Override
    public <T> CompletableFuture<T> async(Supplier<T> supplier) {
        return CompletableFuture.supplyAsync(supplier, EXECUTOR);
    }

    @Override
    public JavaPlugin bootstrap() {
        return bootstrap;
    }
}

package dev.scopped.zenLifesteal.leaderboard;

import dev.scopped.zenLifesteal.LifestealPlugin;
import dev.scopped.zenLifesteal.leaderboard.model.LeaderboardEntry;
import dev.scopped.zenLifesteal.user.models.statistics.StatisticType;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;

public class LeaderboardServiceImpl implements LeaderboardService {

    private final EnumMap<StatisticType, List<LeaderboardEntry>> leaderboards = new EnumMap<>(StatisticType.class);
    private final LifestealPlugin plugin;

    public LeaderboardServiceImpl(LifestealPlugin plugin) {
        this.plugin = plugin;

        for (StatisticType type : StatisticType.VALUES) {
            refreshLeaderboard(type);
        }
    }

    public List<LeaderboardEntry> leaderboardEntries(StatisticType type) {
        synchronized (leaderboards) {
            return leaderboards.get(type);
        }
    }

    @Override
    public void refreshLeaderboard(StatisticType type) {
        plugin.async(() -> plugin.databaseService().leaderboard(type, 20))
                .thenAccept(result -> {
                    List<LeaderboardEntry> copy = new LinkedList<>(result);
                    synchronized (leaderboards) {
                        leaderboards.put(type, copy);
                    }
                })
                .exceptionally(throwable -> {
                    plugin.loggerProvider().error("Failed to refresh leaderboard for " + type.name(), throwable);
                    return null;
                });
    }

    @Override
    public void register() {
        plugin.server().getScheduler().runTaskTimerAsynchronously(plugin.bootstrap(), () -> {
            for (StatisticType type : StatisticType.VALUES) {
                refreshLeaderboard(type);
            }
        }, 20L, 5 * 60 * 20L);
    }

    @Override
    public EnumMap<StatisticType, List<LeaderboardEntry>> loadedLeaderboards() {
        return leaderboards;
    }
}

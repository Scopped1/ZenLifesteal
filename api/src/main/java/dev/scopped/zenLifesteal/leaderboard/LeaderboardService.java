package dev.scopped.zenLifesteal.leaderboard;

import dev.scopped.zenLifesteal.leaderboard.model.LeaderboardEntry;
import dev.scopped.zenLifesteal.user.models.statistics.StatisticType;

import java.util.EnumMap;
import java.util.List;

public interface LeaderboardService {

    List<LeaderboardEntry> leaderboardEntries(StatisticType type);

    void refreshLeaderboard(StatisticType type);

    void register();

    EnumMap<StatisticType, List<LeaderboardEntry>> loadedLeaderboards();

}

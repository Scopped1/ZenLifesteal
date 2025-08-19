package dev.scopped.zenLifesteal.user.model.statistics;

import dev.scopped.zenLifesteal.user.models.statistics.StatisticType;
import dev.scopped.zenLifesteal.user.models.statistics.UserStatistics;

import java.util.HashMap;
import java.util.Map;

public class UserStatisticsImpl extends UserStatistics {

    private final Map<StatisticType, Long> statistics = new HashMap<>();

    public UserStatisticsImpl() {
        for (StatisticType statistic : StatisticType.values()) {
            statistics.put(statistic, 0L);
        }
    }

    @Override
    public Map<StatisticType, Long> loadedStatistics() {
        return statistics;
    }

    @Override
    public long kills() {
        return statistic(StatisticType.KILLS);
    }

    @Override
    public long deaths() {
        return statistic(StatisticType.DEATHS);
    }

    @Override
    public long hearts() {
        return statistic(StatisticType.HEARTS);
    }

    @Override
    public long playtime() {
        return statistic(StatisticType.PLAYTIME);
    }

    @Override
    public long firstJoin() {
        return statistic(StatisticType.FIRST_JOIN);
    }

    @Override
    public long lastJoin() {
        return statistic(StatisticType.LAST_JOIN);
    }

    @Override
    public long statistic(StatisticType type) {
        return statistics.get(type);
    }

    @Override
    public void set(StatisticType type, long amount) {
        statistics.put(type, amount);
    }

    @Override
    public void add(StatisticType type, long amount) {
        set(type, statistic(type) + amount);
    }

    @Override
    public void remove(StatisticType type, long amount) {
        set(type, statistic(type) - amount);
    }
}

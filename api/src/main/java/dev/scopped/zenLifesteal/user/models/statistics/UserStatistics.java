package dev.scopped.zenLifesteal.user.models.statistics;

import java.util.Map;

public abstract class UserStatistics {

    public abstract Map<StatisticType, Long> loadedStatistics();

    public abstract long kills();

    public abstract long deaths();

    public abstract long hearts();

    public abstract long playtime();

    public abstract long firstJoin();

    public abstract long lastJoin();

    public abstract long statistic(StatisticType type);

    public abstract void set(StatisticType type, long amount);

    public abstract void add(StatisticType type, long amount);

    public abstract void remove(StatisticType type, long amount);

}

package dev.scopped.zenLifesteal.user.models.statistics;

public enum StatisticType {

    KILLS("kills"),
    DEATHS("deaths"),
    HEARTS("hearts"),
    PLAYTIME("playtime"),
    FIRST_JOIN("first_join"),
    LAST_JOIN("last_join");

    public static final StatisticType[] VALUES = values();
    private final String id;

    StatisticType(String id) {
        this.id = id;
    }

    public String id() {
        return id;
    }
}

package dev.scopped.zenLifesteal.user.model;

import dev.scopped.zenLifesteal.user.model.statistics.UserStatisticsImpl;
import dev.scopped.zenLifesteal.user.models.LifestealUser;
import dev.scopped.zenLifesteal.user.models.statistics.UserStatistics;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class LifestealUserImpl extends LifestealUser {

    private final int id;

    private final UserStatistics statistics;

    private final long sessionTime;
    private long combatTime;

    public LifestealUserImpl(int id, @NotNull UUID uuid, @NotNull String name) {
        super(uuid, name);
        this.id = id;

        this.statistics = new UserStatisticsImpl();

        this.sessionTime = System.currentTimeMillis();
        this.combatTime = 0;
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public UserStatistics statistics() {
        return statistics;
    }

    @Override
    public long combatTime() {
        return combatTime;
    }

    @Override
    public long sessionTime() {
        return sessionTime;
    }

    @Override
    public long calculatePlayTime() {
        return System.currentTimeMillis() - sessionTime;
    }

    @Override
    public void updateCombatTime(long combatTime) {
        this.combatTime = combatTime;
    }
}

package dev.scopped.zenLifesteal.user.models;

import dev.scopped.zenLifesteal.user.models.statistics.UserStatistics;
import it.ytnoos.loadit.api.UserData;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class LifestealUser extends UserData {

    protected LifestealUser(@NotNull UUID uuid, @NotNull String name) {
        super(uuid, name);
    }

    public abstract int id();

    public abstract UserStatistics statistics();

    public abstract long combatTime();

    public abstract long sessionTime();

    public abstract long calculatePlayTime();

    public abstract void updateCombatTime(long combatTime);

}

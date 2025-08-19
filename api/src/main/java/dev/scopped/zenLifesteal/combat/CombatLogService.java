package dev.scopped.zenLifesteal.combat;

import org.bukkit.entity.Player;

public interface CombatLogService {

    boolean tagged(Player player);

    void tagPlayer(Player player);

    void tag(Player player, Player damager);

    boolean untag(Player player);

    long timeLeft(Player player);

    void flush();

}

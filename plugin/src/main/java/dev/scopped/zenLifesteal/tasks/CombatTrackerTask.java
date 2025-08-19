package dev.scopped.zenLifesteal.tasks;

import dev.scopped.zenLifesteal.LifestealPlugin;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CombatTrackerTask extends BukkitRunnable {

    private final LifestealPlugin plugin;

    public CombatTrackerTask(LifestealPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player onlinePlayer : plugin.bootstrap().getServer().getOnlinePlayers()) {
            long remainingTime = plugin.combatLogService().timeLeft(onlinePlayer);
            if (remainingTime <= 0) continue;

            plugin.messageProvider().actionBar(onlinePlayer, "&fCombat: &c%time%", "time", String.format("%.1f", (double) remainingTime / 1000));
        }
    }

    public void register() {
        runTaskTimerAsynchronously(plugin.bootstrap(), 0, 2L);
    }
}

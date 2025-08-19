package dev.scopped.zenLifesteal.combat;

import dev.scopped.zenLifesteal.LifestealPlugin;
import org.bukkit.entity.Player;

public class CombatLogServiceImpl implements CombatLogService {

    private final LifestealPlugin plugin;

    public CombatLogServiceImpl(LifestealPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean tagged(Player player) {
        return plugin.userService().onlineUser(player).combatTime() > System.currentTimeMillis();
    }

    @Override
    public void tagPlayer(Player player) {
        plugin.userService().onlineUser(player).updateCombatTime(System.currentTimeMillis() + 15_000);
    }

    @Override
    public void tag(Player player, Player damager) {
        if (!tagged(player)) {
            if (damager != null && damager != player) {
                //TODO: Enter in combat message
            } else {
                //TODO: Enter in combat nobody message
            }
        }

        if (damager != null && damager != player) {
            if (!tagged(damager)) {
                //TODO: Enter in combat message
            }

            tagPlayer(damager);
        }

        tagPlayer(player);
    }

    @Override
    public boolean untag(Player player) {
        if (tagged(player)) {
            plugin.userService().onlineUser(player).updateCombatTime(0);
            return true;
        }

        return false;
    }

    @Override
    public long timeLeft(Player player) {
        return Math.max(0, plugin.userService().onlineUser(player).combatTime() - System.currentTimeMillis());
    }

    @Override
    public void flush() {
        for (Player onlinePlayer : plugin.server().getOnlinePlayers()) {
            if (tagged(onlinePlayer)) untag(onlinePlayer);
        }
    }
}

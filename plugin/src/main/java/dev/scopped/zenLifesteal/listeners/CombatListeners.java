package dev.scopped.zenLifesteal.listeners;

import dev.scopped.zenLifesteal.LifestealPlugin;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class CombatListeners implements Listener {

    private final LifestealPlugin plugin;

    public CombatListeners(LifestealPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void entityDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player player)) return;

        Entity damager = event.getDamager();

        if (damager instanceof Projectile projectile) damager = (Entity) projectile.getShooter();
        if (!(damager instanceof Player attacker)) return;

        if (player.equals(attacker)) {
            plugin.combatLogService().tag(player, null);
        } else {
            plugin.combatLogService().tag(player, attacker);
        }
    }

    @EventHandler
    public void commandPreProcess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        String command = event.getMessage().split(" ")[0].toLowerCase();

        if (!plugin.combatLogService().tagged(player) || player.hasPermission("zenlifesteal.bypass")) return;

        List<String> allowedCommands = List.of("/msg", "/hub");
        if (!allowedCommands.contains(command)) {
            //TODO: Cant execute command in combat message
            event.setCancelled(true);
        }
    }

}

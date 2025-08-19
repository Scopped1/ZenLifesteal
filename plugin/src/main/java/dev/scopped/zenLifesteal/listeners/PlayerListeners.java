package dev.scopped.zenLifesteal.listeners;

import dev.scopped.zenLifesteal.LifestealPlugin;
import dev.scopped.zenLifesteal.user.models.LifestealUser;
import dev.scopped.zenLifesteal.user.models.statistics.StatisticType;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListeners implements Listener {

    private final LifestealPlugin plugin;

    public PlayerListeners(LifestealPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        LifestealUser user = plugin.userService().onlineUser(player);

        if (user.statistics().firstJoin() == 0) {
            plugin.userService().setStatistic(player.getName(), StatisticType.FIRST_JOIN, System.currentTimeMillis());
        }
    }

    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (plugin.combatLogService().untag(player)) {
            player.setHealth(0);
        }

        LifestealUser user = plugin.userService().onlineUser(player);

        plugin.userService().setStatistic(player.getName(), StatisticType.LAST_JOIN, System.currentTimeMillis());
        plugin.userService().addStatistic(player.getName(), StatisticType.PLAYTIME, user.calculatePlayTime());
    }

    @EventHandler
    public void playerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        plugin.userService().addStatistic(player.getName(), StatisticType.DEATHS, 1);

        LifestealUser playerUser = plugin.userService().onlineUser(player);

        if (playerUser.statistics().hearts() == 2) {
            plugin.userService().setStatistic(player.getName(), StatisticType.HEARTS, 20).thenAccept(success -> {
                if (!success) return;

                plugin.server().getScheduler().runTask(plugin.bootstrap(), () -> {
                    LifestealUser user = plugin.userService().onlineUser(player);

                    AttributeInstance attribute = player.getAttribute(Attribute.MAX_HEALTH);
                    if (attribute != null) attribute.setBaseValue(user.statistics().hearts());
                });
            });
        } else {
            plugin.userService().removeStatistic(player.getName(), StatisticType.HEARTS, 2).thenAccept(success -> {
                if (!success) return;

                plugin.server().getScheduler().runTask(plugin.bootstrap(), () -> {
                    LifestealUser user = plugin.userService().onlineUser(player);

                    AttributeInstance attribute = player.getAttribute(Attribute.MAX_HEALTH);
                    if (attribute != null) attribute.setBaseValue(user.statistics().hearts());
                });
            });
        }

        Player killer = event.getEntity().getKiller();
        if (killer == null || killer.equals(player)) return;

        plugin.userService().addStatistic(killer.getName(), StatisticType.KILLS, 1);

        plugin.userService().addStatistic(killer.getName(), StatisticType.HEARTS, 2).thenAccept(success -> {
            if (!success) return;

            plugin.server().getScheduler().runTask(plugin.bootstrap(), () -> {
                AttributeInstance attribute = killer.getAttribute(Attribute.MAX_HEALTH);
                if (attribute == null) return;

                LifestealUser user = plugin.userService().onlineUser(killer);
                attribute.setBaseValue(user.statistics().hearts());
            });
        });
    }
}

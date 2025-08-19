package dev.scopped.zenLifesteal.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.scopped.zenLifesteal.LifestealPlugin;
import dev.scopped.zenLifesteal.user.models.statistics.StatisticType;
import org.bukkit.entity.Player;

@CommandAlias("statsedit")
@CommandPermission("zenlifesteal.command.statsedit")
public class StatsEditCommand extends BaseCommand {

    private final LifestealPlugin plugin;

    public StatsEditCommand(LifestealPlugin plugin) {
        this.plugin = plugin;
    }

    @HelpCommand
    public void help(Player player) {
        //TOO: Help commands for stats - message
    }

    @Default
    public void root(Player player) {
        help(player);
    }

    @Subcommand("set")
    @CommandCompletion("@statistics @players")
    public void set(Player player, StatisticType type, String targetName, long amount) {
        Player target = player(player, targetName);
        if (target == null) return;

        plugin.userService().setStatistic(target.getName(), type, amount).thenAccept(success -> {
            if (success) {
                //TODO: Success stats set message
            } else {
                //TODO: Failed stats set message
            }
        });
    }

    @Subcommand("add")
    @CommandCompletion("@statistics @players")
    public void add(Player player, StatisticType type, String targetName, long amount) {
        Player target = player(player, targetName);
        if (target == null) return;

        plugin.userService().addStatistic(target.getName(), type, amount).thenAccept(success -> {
            if (success) {
                //TODO: Success stats set message
            } else {
                //TODO: Failed stats set message
            }
        });
    }

    @Subcommand("remove")
    @CommandCompletion("@statistics @players")
    public void remove(Player player, StatisticType type, String targetName, long amount) {
        Player target = player(player, targetName);
        if (target == null) return;

        plugin.userService().removeStatistic(target.getName(), type, amount).thenAccept(success -> {
            if (success) {
                //TODO: Success stats set message
            } else {
                //TODO: Failed stats set message
            }
        });
    }

    private Player player(Player player, String targetName) {
        Player target = plugin.server().getPlayerExact(targetName);

        if (target == null) {
            //TODO: Player not found message
            return null;
        }

        return target;
    }

}

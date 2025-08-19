package dev.scopped.zenLifesteal.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.scopped.zenLifesteal.LifestealPlugin;
import dev.scopped.zenLifesteal.user.models.statistics.StatisticType;
import org.bukkit.entity.Player;

@CommandAlias("heartsedit")
@CommandPermission("zenlifesteal.command.heartsedit")
public class HeartsEditCommand extends BaseCommand {

    private final LifestealPlugin plugin;

    public HeartsEditCommand(LifestealPlugin plugin) {
        this.plugin = plugin;
    }

    @HelpCommand
    public void help(Player player) {
        //TOO: Help commands for hearts - message
    }

    @Default
    public void root(Player player) {
        help(player);
    }

    @Subcommand("set")
    @CommandCompletion("@players")
    public void set(Player player, String targetName, long amount) {
        Player target = player(player, targetName);
        if (target == null) return;

        plugin.userService().setStatistic(target.getName(), StatisticType.HEARTS, amount).thenAccept(success -> {
            if (success) {
                //TODO: Success hearts set message
            } else {
                //TODO: Failed hearts set message
            }
        });
    }

    @Subcommand("add")
    @CommandCompletion("@players")
    public void add(Player player, String targetName, long amount) {
        Player target = player(player, targetName);
        if (target == null) return;

        plugin.userService().addStatistic(target.getName(), StatisticType.HEARTS, amount).thenAccept(success -> {
            if (success) {
                //TODO: Success hearts set message
            } else {
                //TODO: Failed hearts set message
            }
        });
    }

    @Subcommand("remove")
    @CommandCompletion("@players")
    public void remove(Player player, String targetName, long amount) {
        Player target = player(player, targetName);
        if (target == null) return;

        plugin.userService().removeStatistic(target.getName(), StatisticType.HEARTS, amount).thenAccept(success -> {
            if (success) {
                //TODO: Success hearts set message
            } else {
                //TODO: Failed hearts set message
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

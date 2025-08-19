package dev.scopped.zenLifesteal.hooks.papi;

import dev.scopped.zenLifesteal.LifestealPlugin;
import dev.scopped.zenLifesteal.leaderboard.model.LeaderboardEntry;
import dev.scopped.zenLifesteal.user.models.LifestealUser;
import dev.scopped.zenLifesteal.user.models.statistics.StatisticType;
import dev.scopped.zenLifesteal.user.models.statistics.UserStatistics;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static dev.scopped.zenLifesteal.utility.StringUtils.formatDate;
import static dev.scopped.zenLifesteal.utility.StringUtils.formatTime;

public class PlaceholderHook extends PlaceholderExpansion {

    private static final Pattern TOP_PATTERN = Pattern.compile("^top_([a-zA-Z]+)_(\\d+)(?:_(name))?$");
    private final LifestealPlugin plugin;

    public PlaceholderHook(LifestealPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
        if (params.startsWith("top_")) {
            return handleTop(params);
        }

        LifestealUser user = plugin.userService().onlineUser(player);

        UserStatistics statistics = user.statistics();

        return switch (params) {
            case "kills" -> statistics.kills() + "";
            case "deaths" -> statistics.deaths() + "";
            case "hearts" -> statistics.hearts() + "";
            case "first_join" -> formatDate(statistics.firstJoin());
            case "last_join" -> formatDate(statistics.lastJoin());
            case "playtime" -> formatTime(user.calculatePlayTime() / 1000);
            default -> "[ZenLifesteal] Placeholder not found";
        };
    }

    private String handleTop(String params) {
        Matcher matcher = TOP_PATTERN.matcher(params);
        if (!matcher.matches()) return "";

        try {
            StatisticType type = StatisticType.valueOf(matcher.group(1).toUpperCase());
            int index = Integer.parseInt(matcher.group(2)) - 1;
            boolean isName = matcher.group(3) != null;

            List<LeaderboardEntry> entries = plugin.leaderboardService().leaderboardEntries(type);
            if (index < 0 || index >= entries.size()) return "---";

            LeaderboardEntry entry = entries.get(index);
            return isName ? entry.name() : String.valueOf(entry.amount());
        } catch (Exception e) {
            return "---";
        }
    }

    @Override
    public @NotNull String getIdentifier() {
        return "zenlifesteal";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Scopped_";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }
}

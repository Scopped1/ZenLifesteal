package dev.scopped.zenLifesteal.providers;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.List;

import static dev.scopped.zenLifesteal.utility.StringUtils.replace;

public interface MessageProvider {

    Component translate(String message, Object... replacements);

    default Component translate(String message, Player player, Object... replacements) {
        return translate(PlaceholderAPI.setPlaceholders(player, replace(message, replacements)));
    }

    default List<Component> translate(List<String> messages, Object... replacements) {
        return messages.stream().map(message -> translate(message, replacements)).toList();
    }

    default List<Component> translate(List<String> messages, Player player, Object... replacements) {
        return messages.stream().map(message -> translate(message, player, replacements)).toList();
    }

    default void send(Player player, String message, Object... replacements) {
        player.sendMessage(translate(message, player, replacements));
    }

    default void send(Player player, List<String> messages, Object... replacements) {
        messages.forEach(message -> send(player, message, replacements));
    }

    default void actionBar(Player player, String message, Object... replacements) {
        player.sendActionBar(translate(message, player, replacements));
    }

    default void title(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut, Object... replacements) {
        player.showTitle(
                Title.title(
                        translate(title, player, replacements),
                        translate(subtitle, player, replacements),
                        Title.Times.times(
                                Duration.ofSeconds(fadeIn),
                                Duration.ofSeconds(stay),
                                Duration.ofSeconds(fadeOut)
                        )
                )
        );
    }

    default void title(Player player, String title, String subtitle, Object... replacements) {
        title(player, title, subtitle, 1, 2, 1, replacements);
    }

}

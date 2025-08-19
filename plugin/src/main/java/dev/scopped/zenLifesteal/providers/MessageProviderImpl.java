package dev.scopped.zenLifesteal.providers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;

import static dev.scopped.zenLifesteal.utility.StringUtils.replace;

public class MessageProviderImpl implements MessageProvider {

    public static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    @Override
    public Component translate(String message, Object... replacements) {
        return MINI_MESSAGE.deserialize(replace(message, replacements))
                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

}

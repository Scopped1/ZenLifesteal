package dev.scopped.zenLifesteal.config;

import dev.scopped.zenLifesteal.LifestealAPI;
import net.pino.simpleconfig.BaseConfig;
import net.pino.simpleconfig.annotations.Config;
import net.pino.simpleconfig.annotations.ConfigFile;

@Config
@ConfigFile("messages.yml")
public class MessagesConfig extends BaseConfig {

    public MessagesConfig(LifestealAPI plugin) {
        registerConfig(plugin.bootstrap());
    }

}

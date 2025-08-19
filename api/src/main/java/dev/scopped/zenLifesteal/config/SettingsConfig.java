package dev.scopped.zenLifesteal.config;

import dev.scopped.zenLifesteal.LifestealAPI;
import net.pino.simpleconfig.BaseConfig;
import net.pino.simpleconfig.annotations.Config;
import net.pino.simpleconfig.annotations.ConfigFile;
import net.pino.simpleconfig.annotations.inside.Path;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

@Config
@ConfigFile("settings.yml")
public class SettingsConfig extends BaseConfig {

    @Path("mysql.hostname")
    public String mysqlHostname = "localhost";
    @Path("mysql.port")
    public int mysqlPort = 3306;
    @Path("mysql.database")
    public String mysqlDatabase = "zen_lifesteal";
    @Path("mysql.username")
    public String mysqlUsername = "zen_lifesteal_user";
    @Path("mysql.password")
    public String mysqlPassword = "zen_lifesteal_password";

    public SettingsConfig(LifestealAPI plugin) {
        registerConfig(plugin.bootstrap());
    }

    public File file() {
        return configFile;
    }

    public FileConfiguration config() {
        return fileConfiguration;
    }

}

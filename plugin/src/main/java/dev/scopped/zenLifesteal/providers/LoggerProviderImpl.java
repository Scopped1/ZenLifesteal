package dev.scopped.zenLifesteal.providers;

import org.bukkit.Bukkit;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerProviderImpl implements LoggerProvider {

    private final Logger LOGGER = Bukkit.getLogger();

    @Override
    public void info(String message) {
        LOGGER.info(message);
    }

    @Override
    public void warn(String message) {
        LOGGER.warning(message);
    }

    @Override
    public void error(String message) {
        LOGGER.severe(message);
    }

    @Override
    public void error(String message, Throwable e) {
        LOGGER.log(Level.SEVERE, message, e);
    }
}

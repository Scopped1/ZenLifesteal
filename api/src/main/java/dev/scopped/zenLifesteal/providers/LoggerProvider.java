package dev.scopped.zenLifesteal.providers;

public interface LoggerProvider {

    void info(String message);

    void warn(String message);

    void error(String message);

    void error(String message, Throwable e);

}

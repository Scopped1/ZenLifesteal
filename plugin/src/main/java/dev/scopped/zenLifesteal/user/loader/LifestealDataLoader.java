package dev.scopped.zenLifesteal.user.loader;

import dev.scopped.zenLifesteal.LifestealPlugin;
import dev.scopped.zenLifesteal.user.models.LifestealUser;
import it.ytnoos.loadit.api.DataLoader;

import java.util.Optional;
import java.util.UUID;

public class LifestealDataLoader implements DataLoader<LifestealUser> {

    private final LifestealPlugin plugin;

    public LifestealDataLoader(LifestealPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Optional<LifestealUser> getOrCreate(UUID uuid, String s) {
        return Optional.empty();
    }

    @Override
    public Optional<LifestealUser> load(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public Optional<LifestealUser> load(String s) {
        return Optional.empty();
    }
}

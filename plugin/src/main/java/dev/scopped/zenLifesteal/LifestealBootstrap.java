package dev.scopped.zenLifesteal;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class LifestealBootstrap extends JavaPlugin {

    private LifestealPlugin plugin;

    @Override
    public void onEnable() {
        try {
            this.plugin = new LifestealPlugin(this);
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to enable plugin", e);
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        if (this.plugin != null) this.plugin.disable();
    }
}

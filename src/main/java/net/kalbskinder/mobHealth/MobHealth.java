package net.kalbskinder.mobHealth;

import net.kalbskinder.mobHealth.configuration.Config;
import org.bukkit.plugin.java.JavaPlugin;

public final class MobHealth extends JavaPlugin {

    @Override
    public void onEnable() {
        new Config(getConfig());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

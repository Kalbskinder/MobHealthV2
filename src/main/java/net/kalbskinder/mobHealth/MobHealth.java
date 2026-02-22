package net.kalbskinder.mobHealth;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.extern.slf4j.Slf4j;
import net.kalbskinder.mobHealth.commands.MobHealthCommand;
import net.kalbskinder.mobHealth.configuration.Config;
import org.bukkit.plugin.java.JavaPlugin;

@Slf4j
public final class MobHealth extends JavaPlugin {

    private void registerCommands() {
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commnads -> {
            commnads.registrar().register(MobHealthCommand.mobHealthCommand());
        });
    }

    private void startUpMessage() {
        log.info("-------------------------------");
        log.info("            MobHealth       ");
        log.info("         Version: 2.0.0");
        log.info("       Author: Kalbskinder");
        log.info("--------------------------------");
    }

    @Override
    public void onEnable() {
        new Config(this);
        registerCommands();

        saveDefaultConfig();
        startUpMessage();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

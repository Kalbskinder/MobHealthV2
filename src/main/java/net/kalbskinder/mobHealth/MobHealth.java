package net.kalbskinder.mobHealth;

import dev.faststats.bukkit.BukkitMetrics;
import dev.faststats.core.ErrorTracker;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.kalbskinder.mobHealth.commands.MobHealthCommand;
import net.kalbskinder.mobHealth.configuration.Config;
import net.kalbskinder.mobHealth.listeners.AnyEntityDespawnListener;
import net.kalbskinder.mobHealth.listeners.EntityDamageListener;
import net.kalbskinder.mobHealth.listeners.EntitySpawnListener;
import net.kalbskinder.mobHealth.listeners.PlayerRenameEntityListener;
import net.kalbskinder.mobHealth.service.RenameMobService;
import net.kalbskinder.mobHealth.util.HealthColorUtil;
import net.kalbskinder.mobHealth.util.TextUtil;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Slf4j
public final class MobHealth extends JavaPlugin {

    public static final ErrorTracker ERROR_TRACKER = ErrorTracker.contextAware();

    private final HealthColorUtil healthColorUtil = new HealthColorUtil();
    private final TextUtil textUtil = new TextUtil();
    private final RenameMobService renameMobService = new RenameMobService(healthColorUtil, textUtil, this);

    private final BukkitMetrics metrics = BukkitMetrics.factory()
            .token("")
            .errorTracker(ERROR_TRACKER)
            .create(this);

    @Getter private static MobHealth instance;


    private void registerCommands() {
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commnads -> {
            commnads.registrar().register(MobHealthCommand.mobHealthCommand());
        });
    }

    private void registerListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new EntitySpawnListener(renameMobService), this);
        pm.registerEvents(new EntityDamageListener(renameMobService), this);
        pm.registerEvents(new PlayerRenameEntityListener(renameMobService), this);
        pm.registerEvents(new AnyEntityDespawnListener(), this);
    }

    private void startUpMessage() {
        log.info("-------------------------------");
        log.info("            MobHealth       ");
        log.info("         Version: {}", getPluginMeta().getVersion());
        log.info("       Author: Kalbskinder");
        log.info("--------------------------------");
    }

    @Override
    public void onEnable() {
        instance = this;

        new Config(this);

        registerListeners();
        registerCommands();

        saveDefaultConfig();
        startUpMessage();

        metrics.ready();
    }

    @Override
    public void onDisable() {
        metrics.shutdown();
    }
}

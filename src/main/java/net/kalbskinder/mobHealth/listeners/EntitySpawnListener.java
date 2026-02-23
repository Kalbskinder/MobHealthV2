package net.kalbskinder.mobHealth.listeners;

import lombok.RequiredArgsConstructor;
import net.kalbskinder.mobHealth.configuration.Config;
import net.kalbskinder.mobHealth.service.RenameMobService;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.world.ChunkLoadEvent;

@RequiredArgsConstructor
public class EntitySpawnListener implements Listener {

    private final RenameMobService renameMobService;

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof LivingEntity entity) {
            renameMobService.renameMob(entity, Config.SELECTED_PROFILE());
        }
    }

    @EventHandler
    public void test(ChunkLoadEvent event) {
        for (Entity entity : event.getChunk().getEntities()) {
            if (entity instanceof LivingEntity livingEntity) {
                renameMobService.renameMob(livingEntity, Config.SELECTED_PROFILE());
            }
        }
    }

}

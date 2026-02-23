package net.kalbskinder.mobHealth.listeners;

import lombok.RequiredArgsConstructor;
import net.kalbskinder.mobHealth.MobHealth;
import net.kalbskinder.mobHealth.configuration.Config;
import net.kalbskinder.mobHealth.service.RenameMobService;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

@RequiredArgsConstructor
public class EntityDamageListener implements Listener {

    private final RenameMobService renameMobService;

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof LivingEntity entity) {
            if (entity.getHealth() - event.getFinalDamage() <= 0) {
                entity.setCustomName(null);
                entity.setCustomNameVisible(false);
                return;
            }

            // Schedule for next tick so health has already been reduced before we read it
            Bukkit.getScheduler().runTask(MobHealth.getInstance(), () -> {
                if (entity.isValid()) {
                    renameMobService.renameMob(entity, Config.SELECTED_PROFILE());
                }
            });
        }
    }
}

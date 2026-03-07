package net.kalbskinder.mobHealth.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;

public class AnyEntityDespawnListener implements Listener {

    private void removeEntityPassengers(Entity entity) {
        entity.getPassengers().stream()
                .filter(e -> e instanceof TextDisplay)
                .forEach(Entity::remove);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        removeEntityPassengers(event.getEntity());
    }

    @EventHandler
    public void onEntityEnterBlock(EntityEnterBlockEvent event) {
        removeEntityPassengers(event.getEntity());
    }

    @EventHandler
    public void onEntityEnterPortal(EntityPortalEnterEvent event) {
        removeEntityPassengers(event.getEntity());
    }

    @EventHandler
    public void onEntityDismount(EntityDismountEvent event) {
        if (event.getDismounted() instanceof TextDisplay) {
            event.getDismounted().remove();
        }
    }

    @EventHandler
    public void onCreeperWillExplode(ExplosionPrimeEvent event) {
        removeEntityPassengers(event.getEntity());
    }
}

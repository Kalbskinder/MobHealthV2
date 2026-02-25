package net.kalbskinder.mobHealth.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeathListener implements Listener {
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        event.getEntity().getPassengers().stream()
                .filter(e -> e instanceof TextDisplay)
                .forEach(Entity::remove);
    }
}

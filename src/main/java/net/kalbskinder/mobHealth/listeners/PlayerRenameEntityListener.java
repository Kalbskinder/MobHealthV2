package net.kalbskinder.mobHealth.listeners;

import lombok.RequiredArgsConstructor;
import net.kalbskinder.mobHealth.MobHealth;
import net.kalbskinder.mobHealth.configuration.Config;
import net.kalbskinder.mobHealth.service.RenameMobService;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

@RequiredArgsConstructor
public class PlayerRenameEntityListener implements Listener {

    private final RenameMobService renameMobService;

    @EventHandler
    public void onPlayerRenameEntity(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof LivingEntity entity)) {
            return;
        }

        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if (item.getType() != Material.NAME_TAG || !item.hasItemMeta()) {
            return;
        }

        ItemMeta meta = item.getItemMeta();
        if (!meta.hasDisplayName()) {
            return;
        }

        String newName = PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(meta.displayName()));
        NamespacedKey key = new NamespacedKey(MobHealth.getInstance(), "custom_mob_name");
        entity.getPersistentDataContainer().set(key, PersistentDataType.STRING, newName);

        renameMobService.renameMob(entity, Config.SELECTED_PROFILE());
    }
}

package net.kalbskinder.mobHealth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.kalbskinder.mobHealth.MobHealth;
import net.kalbskinder.mobHealth.configuration.Config;
import net.kalbskinder.mobHealth.enums.DisplaySetting;
import net.kalbskinder.mobHealth.util.HealthColorUtil;
import net.kalbskinder.mobHealth.util.TextUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;

@Slf4j
@RequiredArgsConstructor
public class RenameMobService {

    private final HealthColorUtil healthColorUtil;
    private final TextUtil textUtil;

    public double getDefaultMaxHealth(LivingEntity entity) {
        return entity.getAttribute(Attribute.MAX_HEALTH).getValue();
    }

    public void renameMob(Entity entity, DisplaySetting activeDisplaySetting) {
        String mobName;
        entity.setCustomNameVisible(Config.General.ALWAYS_SHOW_NAME());

        NamespacedKey key = new NamespacedKey(MobHealth.getInstance(), "custom_mob_name");

        if (entity.getPersistentDataContainer().get(key, PersistentDataType.STRING) == null) {
            String typeName = entity.getType().name().toLowerCase().replace('_', ' ');
            mobName = Character.toUpperCase(typeName.charAt(0)) + typeName.substring(1);
        } else {
            mobName = entity.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        }

        entity.setCustomName(null); // Clear existing name to prevent duplication

        // Map to living entity to access health and max health
        if (entity instanceof LivingEntity livingEntity) {
            switch (activeDisplaySetting) {
                case DisplaySetting.SKYBLOCK -> renameMobSkyblock(livingEntity, mobName);
                case DisplaySetting.HEARTS_SPRITE -> renameMobHeartsSprite(livingEntity, mobName);
                case DisplaySetting.HEARTS_SYMBOLS -> renameMobHeartsSymbol(livingEntity, mobName);
                case DisplaySetting.SQUARES -> renameMobSquares(livingEntity, mobName);
                case DisplaySetting.BARS -> renameMobHealthBar(livingEntity, mobName);
            }
        }
    }

    private void renameMobSkyblock(LivingEntity entity, String mobName) {
        double health = entity.getHealth();
        double roundedHealth = Math.round(health * 10.0) / 10.0;
        if (roundedHealth < 0) roundedHealth = 0;
        double maxHealth = getDefaultMaxHealth(entity);
        double roundedMaxHealth = Math.round(maxHealth * 10.0) / 10.0;

        String healthColor = healthColorUtil.getSkyblockHealthColor(health, maxHealth);
        String prefix = Config.HealthBars.Skyblock.PREFIX();
        String suffix = Config.HealthBars.Skyblock.SUFFIX();

        String healthBarDisplay = String.format("%s%.1f&7/&a%.1f", healthColor, roundedHealth, roundedMaxHealth);

        if (Config.HealthBars.Skyblock.DISABLE_MOB_NAME()) {
            entity.setCustomName(textUtil.parseLegacy(healthBarDisplay));
        } else {
            String newMobName = Config.HealthBars.Skyblock.COLOR_NAME() + mobName;

            String mobDisplayName = String.format("%s%s %s%s", prefix, newMobName, healthBarDisplay, suffix);
            entity.setCustomName(textUtil.parseLegacy(mobDisplayName));
        }
    }

    private void renameMobHeartsSprite(Entity entity, String mobName) {
        // Implement the logic to rename the mob according to the Hearts display setting
    }

    public void renameMobHeartsSymbol(Entity entity, String mobName) {
        // Implement the logic to rename the mob according to the Hearts Symbol display setting
    }

    private void renameMobSquares(Entity entity, String mobName) {
        // Implement the logic to rename the mob according to the Squares display setting
    }

    private void renameMobHealthBar(Entity entity, String mobName) {
        // Implement the logic to rename the mob according to the Health Bar display setting
    }
}

package net.kalbskinder.mobHealth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.kalbskinder.mobHealth.configuration.Config;
import net.kalbskinder.mobHealth.enums.DisplaySetting;
import net.kalbskinder.mobHealth.util.HealthColorUtil;
import net.kalbskinder.mobHealth.util.TextUtil;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class RenameMobService {

    private final HealthColorUtil healthColorUtil;
    private final TextUtil textUtil;

    public void renameMob(Entity entity, DisplaySetting activeDisplaySetting) {
        String mobName = entity.getName();
        log.info("Renaming mob: " + mobName);

        entity.setCustomNameVisible(Config.General.ALWAYS_SHOW_NAME());

        // Map to living entity to access health and max health
        if (entity instanceof LivingEntity livingEntity) {
            switch (activeDisplaySetting) {
                case DisplaySetting.SKYBLOCK -> renameMobSkyblock(livingEntity);
                case DisplaySetting.HEARTS -> renameMobHearts(livingEntity);
                case DisplaySetting.SQUARES -> renameMobSquares(livingEntity);
            }
        }
    }

    private void renameMobSkyblock(LivingEntity entity) {

        double health = entity.getHealth();
        double roundedHealth = Math.round(health * 10.0) / 10.0;
        double maxHealth = Objects.requireNonNull(entity.getAttribute(Attribute.MAX_HEALTH)).getDefaultValue();
        String healthColor = healthColorUtil.getSkyblockHealthColor(health, maxHealth);
        String prefix = Config.HealthBars.Skyblock.PREFIX();
        String suffix = Config.HealthBars.Skyblock.SUFFIX();

        String healthBarDisplay = String.format("%s%f&7/&a%f", healthColor, roundedHealth, maxHealth);

        if (Config.HealthBars.Skyblock.DISABLE_MOB_NAME()) {
            entity.setCustomName(textUtil.legacyToMiniMessage(healthBarDisplay));
        } else {
            String newMobName = Config.HealthBars.Skyblock.COLOR_NAME() + entity.getName();

            String mobDisplayName = String.format("%s%s %s%s", prefix, newMobName, healthBarDisplay, suffix);
            entity.setCustomName(textUtil.legacyToMiniMessage(mobDisplayName));
        }
    }

    private void renameMobHearts(Entity entity) {
        // Implement the logic to rename the mob according to the Hearts display setting
    }

    private void renameMobSquares(Entity entity) {
        // Implement the logic to rename the mob according to the Squares display setting
    }
}

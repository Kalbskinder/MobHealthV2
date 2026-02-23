package net.kalbskinder.mobHealth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.kalbskinder.mobHealth.MobHealth;
import net.kalbskinder.mobHealth.configuration.Config;
import net.kalbskinder.mobHealth.enums.DisplaySetting;
import net.kalbskinder.mobHealth.model.EntityProperties;
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

    /** Rounds a value to the nearest 0.5 */
    private double roundToHalf(double value) {
        return Math.round(value * 2) / 2.0;
    }

    /** Formats a 0.5-rounded value: shows no decimal when it's a whole number, otherwise one decimal */
    private String formatHalf(double value) {
        return value == Math.floor(value) ? String.valueOf((long) value) : String.format("%.1f", value);
    }

    public void renameMob(Entity anyEntity, DisplaySetting activeDisplaySetting) {
        if (!(anyEntity instanceof LivingEntity entity)) return;

        String mobName;
        NamespacedKey key = new NamespacedKey(MobHealth.getInstance(), "custom_mob_name");
        if (entity.getPersistentDataContainer().get(key, PersistentDataType.STRING) == null) {
            String typeName = entity.getType().name().toLowerCase().replace('_', ' ');
            mobName = Character.toUpperCase(typeName.charAt(0)) + typeName.substring(1);
        } else {
            mobName = entity.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        }

        double health = entity.getHealth();
        double maxHealth = getDefaultMaxHealth(entity);
        String levelPrefix = String.format("&8[&7Lv%d&8]", (int) Math.ceil(maxHealth * 5 / 20));
        boolean isLevelPrefix = Config.General.DISPLAY_SKYBLOCK_LEVEL();

        EntityProperties entityProperties = new EntityProperties(mobName, health, maxHealth, levelPrefix, isLevelPrefix);

        entity.setCustomNameVisible(Config.General.ALWAYS_SHOW_NAME());
        entity.setCustomName(null); // Clear existing name to prevent duplication

        // Map to living entity to access health and max health
        switch (activeDisplaySetting) {
            case DisplaySetting.SKYBLOCK -> renameMobSkyblock(entity, entityProperties);
            case DisplaySetting.HEARTS_SPRITE -> renameMobHeartsSprite(entity, entityProperties);
            case DisplaySetting.HEARTS_SYMBOLS -> renameMobHeartsSymbol(entity, entityProperties);
            case DisplaySetting.SQUARES -> renameMobSquares(entity, entityProperties);
            case DisplaySetting.BARS -> renameMobHealthBar(entity, entityProperties);
        }
    }

    private void renameMobSkyblock(LivingEntity entity, EntityProperties props) {

        String healthColor = healthColorUtil.getSkyblockHealthColor(props.health(), props.maxHealth());
        String prefix = Config.HealthBars.Skyblock.PREFIX();
        String suffix = Config.HealthBars.Skyblock.SUFFIX();

        if (props.levelPrefixEnabled()) {
            prefix = "%s %s".formatted(props.levelPrefix(), prefix);
        }

        String healthBarDisplay;
        if (Config.HealthBars.Skyblock.DISPLAY_SKYBLOCK_HEALTH()) {
            // Skyblock mode: whole numbers only, multiplied by 5
            long roundedHealth = Math.max(0, Math.round(props.health()));
            long roundedMaxHealth = Math.round(props.maxHealth());
            long skyblockHealth = roundedHealth * 5;
            long skyblockMaxHealth = roundedMaxHealth * 5;
            healthBarDisplay = String.format("%s%d&7/&a%d", healthColor, skyblockHealth, skyblockMaxHealth);
        } else {
            // Normal mode: round to nearest 0.5
            double roundedHealth = Math.max(0, roundToHalf(props.health()));
            double roundedMaxHealth = roundToHalf(props.maxHealth());
            healthBarDisplay = String.format("%s%s&7/&a%s", healthColor, formatHalf(roundedHealth), formatHalf(roundedMaxHealth));
        }

        if (Config.HealthBars.Skyblock.DISABLE_MOB_NAME()) {
            String mobDisplayName = String.format("%s%s%s", prefix, healthBarDisplay, suffix);
            entity.setCustomName(textUtil.parseLegacy(mobDisplayName));
        } else {
            String newMobName = Config.HealthBars.Skyblock.COLOR_NAME() + props.mobName();

            String mobDisplayName = String.format("%s%s %s%s", prefix, newMobName, healthBarDisplay, suffix);
            entity.setCustomName(textUtil.parseLegacy(mobDisplayName));
        }
    }

    private void renameMobHeartsSprite(Entity entity, EntityProperties props) {
        // Implement the logic to rename the mob according to the Hearts display setting
    }

    public void renameMobHeartsSymbol(Entity entity, EntityProperties props) {
        // Implement the logic to rename the mob according to the Hearts Symbol display setting
    }

    private void renameMobSquares(Entity entity, EntityProperties props) {
        // Implement the logic to rename the mob according to the Squares display setting
    }

    private void renameMobHealthBar(Entity entity, EntityProperties props) {
        // Implement the logic to rename the mob according to the Health Bar display setting
    }
}

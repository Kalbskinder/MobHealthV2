package net.kalbskinder.mobHealth.service;

import lombok.extern.slf4j.Slf4j;
import net.kalbskinder.mobHealth.MobHealth;
import net.kalbskinder.mobHealth.configuration.Config;
import net.kalbskinder.mobHealth.enums.DisplaySetting;
import net.kalbskinder.mobHealth.model.EntityProperties;
import net.kalbskinder.mobHealth.util.HealthColorUtil;
import net.kalbskinder.mobHealth.util.TextUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataType;

@Slf4j
public class RenameMobService {

    private final HealthColorUtil healthColorUtil;
    private final TextUtil textUtil;
    private final NamespacedKey healthDisplayKey;

    public RenameMobService(HealthColorUtil healthColorUtil, TextUtil textUtil, MobHealth instance) {
        this.healthColorUtil = healthColorUtil;
        this.textUtil = textUtil;
        this.healthDisplayKey = new NamespacedKey(instance, "mob_health_display");
    }

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
        String levelPrefix = String.format("&8[&7Lv%d&8]&r", (int) Math.ceil(maxHealth * 5 / 20));
        boolean isLevelPrefix = Config.General.DISPLAY_SKYBLOCK_LEVEL();
        boolean isDisableMobName = Config.General.DISABLE_MOB_NAME();
        String mobNameColor = Config.General.NAME_COLOR();

        EntityProperties entityProperties = new EntityProperties(
                mobName,
                mobNameColor,
                health,
                maxHealth,
                levelPrefix,
                isLevelPrefix,
                isDisableMobName
        );

        entity.setCustomNameVisible(Config.General.ALWAYS_SHOW_NAME());
        entity.setCustomName(null); // Clear existing name to prevent duplication

        // Map to living entity to access health and max health
        switch (activeDisplaySetting) {
            case DisplaySetting.SKYBLOCK -> renameMobSkyblock(entity, entityProperties);
            case DisplaySetting.SPRITES -> renameMobSprite(entity, entityProperties);
            case DisplaySetting.SYMBOLS -> renameMobSymbol(entity, entityProperties);
            case DisplaySetting.BARS -> renameMobHealthBar(entity, entityProperties);
        }
    }

    private void renameMobSkyblock(LivingEntity entity, EntityProperties props) {
        resetMobName(entity);

        String healthColor = healthColorUtil.getSkyblockHealthColor(props.health(), props.maxHealth());
        String prefix = Config.HealthBars.Skyblock.PREFIX();
        String suffix = Config.HealthBars.Skyblock.SUFFIX();

        if (props.levelPrefixEnabled()) {
            prefix = "%s %s".formatted(props.levelPrefix(), prefix);
        }

        String healthBarDisplay;
        if (Config.HealthBars.Skyblock.DISPLAY_SKYBLOCK_HEALTH()) {
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

        formatMobDisplayName(entity, props, healthBarDisplay, prefix, suffix);
    }

    private void renameMobSprite(Entity entity, EntityProperties props) {
        resetMobName(entity);

        int totalHalfHeartsHealth = (int) Math.round(props.health());
        int fullSprites = totalHalfHeartsHealth / 2;
        boolean isHalfHealth = totalHalfHeartsHealth % 2 != 0;

        String fullHeartSprite = Config.HealthBars.Sprites.SPRITES_FULL();
        String halfHeartSprite = Config.HealthBars.Sprites.SPRITES_HALF();

        java.util.List<String> spriteTokens = new java.util.ArrayList<>();
        for (int i = 0; i < fullSprites; i++) spriteTokens.add(fullHeartSprite);
        if (isHalfHealth) spriteTokens.add(halfHeartSprite);

        int wrapAt = Config.HealthBars.Sprites.WRAP_AT();
        String heartsDisplay;
        if (wrapAt > 0 && spriteTokens.size() > wrapAt) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < spriteTokens.size(); i++) {
                if (i > 0 && i % wrapAt == 0) sb.append("\n");
                sb.append(spriteTokens.get(i));
            }
            heartsDisplay = sb.toString();
        } else {
            heartsDisplay = String.join("", spriteTokens);
        }

        String prefix = Config.HealthBars.Sprites.PREFIX();
        String suffix = Config.HealthBars.Sprites.SUFFIX();

        if (props.levelPrefixEnabled()) {
            prefix = "%s%s".formatted(props.levelPrefix(), prefix);
        }

        if (!props.disableMobName()) {
            String newMobName = props.nameColor() + props.mobName();
            prefix = "%s %s".formatted(prefix, newMobName);
        }

        String convertedPrefix = textUtil.legacyToMiniMessage(prefix);
        String convertedSuffix = textUtil.legacyToMiniMessage(suffix);
        String displayName = "%s\n%s%s".formatted(convertedPrefix, heartsDisplay, convertedSuffix);

        World world = entity.getWorld();
        String entityId = entity.getUniqueId().toString();

        // Remove any lingering background display from previous implementation
        TextDisplay textDisplay = world.getEntities().stream()
                .filter(e -> e instanceof TextDisplay)
                .map(e -> (TextDisplay) e)
                .filter(e -> e.getPersistentDataContainer().has(healthDisplayKey, PersistentDataType.STRING)
                        && entityId.equals(e.getPersistentDataContainer().get(healthDisplayKey, PersistentDataType.STRING)))
                .findFirst()
                .orElse(null);

        if (textDisplay == null) {
            textDisplay = (TextDisplay) world.spawnEntity(entity.getLocation(), EntityType.TEXT_DISPLAY);
            textDisplay.getPersistentDataContainer().set(healthDisplayKey, PersistentDataType.STRING, entityId);
        }

        textDisplay.text(textUtil.parse(displayName));
        textDisplay.setBillboard(Display.Billboard.CENTER);
        textDisplay.setLineWidth(Config.HealthBars.Sprites.LINE_WIDTH());

        if (!entity.getPassengers().contains(textDisplay)) {
            entity.addPassenger(textDisplay);
        }
    }

    public void renameMobSymbol(Entity entity, EntityProperties props) {
        resetMobName(entity);
        String symbol = Config.HealthBars.Symbols.SYMBOL();
        String healthColor = healthColorUtil.getSymbolHealthColor(props.health(), props.maxHealth());
        String prefix = Config.HealthBars.Symbols.PREFIX();
        String suffix = Config.HealthBars.Symbols.SUFFIX();

        if (props.levelPrefixEnabled()) {
            prefix = "%s %s".formatted(props.levelPrefix(), prefix);
        }

        // 1 symbol per 2 HP, max 20, minimum 1
        int symbolsToShow = (int) Math.ceil(props.health() / 2.0);
        symbolsToShow = Math.max(1, Math.min(symbolsToShow, 20));
        String symbols = symbol.repeat(symbolsToShow);

        String healthBarDisplay = String.format("%s%s", healthColor, symbols);

        formatMobDisplayName(entity, props, healthBarDisplay, prefix, suffix);
    }

    private void renameMobHealthBar(Entity entity, EntityProperties props) {
        // Implement the logic to rename the mob according to the Health Bar display setting
    }

    private void formatMobDisplayName(Entity entity, EntityProperties props, String healthBarDisplay, String prefix, String suffix) {
        if (props.disableMobName()) {
            String mobDisplayName = String.format("%s%s%s", prefix, healthBarDisplay, suffix);
            entity.setCustomName(textUtil.parseLegacy(mobDisplayName));
        } else {
            String newMobName = props.nameColor() + props.mobName();

            String mobDisplayName = String.format("%s%s %s%s", prefix, newMobName, healthBarDisplay, suffix);
            entity.setCustomName(textUtil.parseLegacy(mobDisplayName));
        }
    }

    private void resetMobName(Entity entity) {
        String entityId = entity.getUniqueId().toString();
        entity.getPassengers().stream()
                .filter(e -> e instanceof TextDisplay)
                .map(e -> (TextDisplay) e)
                .filter(e -> e.getPersistentDataContainer().has(healthDisplayKey, PersistentDataType.STRING)
                        && entityId.equals(e.getPersistentDataContainer().get(healthDisplayKey, PersistentDataType.STRING)))
                .forEach(Entity::remove);

        entity.setCustomName(null);
    }
}

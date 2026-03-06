package net.kalbskinder.mobHealth.configuration;

import net.kalbskinder.mobHealth.enums.DisplaySetting;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class Config {
    private static FileConfiguration config;
    private static JavaPlugin plugin;

    public Config(JavaPlugin plugin) {
        Config.plugin = plugin;
        plugin.saveDefaultConfig();
        Config.config = plugin.getConfig();
    }

    protected static Object getConfigValue(String key) {
        return config.get(key, "");
    }

    protected static boolean getConfigBoolean(String key) {
        return config.getBoolean(key, false);
    }

    protected static String getConfigString(String key) {
        return config.getString(key, "");
    }

    protected static int getConfigInt(String key) {
        return config.getInt(key, 0);
    }

    protected static List<String> getConfigStringList(String key) {
        return config.getStringList(key);
    }

    public static void updateSelectedProfile(DisplaySetting newSetting) {
        config.set("selected", newSetting.name());
        plugin.saveConfig();
    }

    public static DisplaySetting SELECTED_PROFILE() {
        String selected = config.getString("selected", DisplaySetting.SKYBLOCK.name());
        try {
            return DisplaySetting.valueOf(selected.toUpperCase());
        } catch (IllegalArgumentException e) {
            return DisplaySetting.SKYBLOCK; // Default to SKYBLOCK if invalid
        }
    }

    public static class General {
        private static final String PATH = "general.";
        public static boolean ALWAYS_SHOW_NAME() { return getConfigBoolean(PATH + "name-always-shown"); }
        public static boolean DISPLAY_SKYBLOCK_LEVEL() { return getConfigBoolean(PATH + "display-skyblock-level"); }
        public static boolean DISABLE_MOB_NAME() { return getConfigBoolean(PATH + "disable-mob-name"); }
        public static String NAME_COLOR() { return getConfigString(PATH + "name-color"); }
    }

    public static class HealthBars {
        public static class Skyblock {
            private static final String PATH = "bars.skyblock.";
            public static String PREFIX() { return getConfigString(PATH + "prefix"); }
            public static String SUFFIX() { return getConfigString(PATH + "suffix"); }
            public static String COLOR_HIGH() { return getConfigString(PATH + "color.high"); }
            public static String COLOR_MID()  { return getConfigString(PATH + "color.mid"); }
            public static String COLOR_LOW()  { return getConfigString(PATH + "color.low"); }
            public static boolean DISPLAY_SKYBLOCK_HEALTH() { return getConfigBoolean(PATH + "display-skyblock-health"); }
        }

        public static class Sprites {
            private static final String PATH = "bars.sprites.";
            public static String PREFIX() { return getConfigString(PATH + "prefix"); }
            public static String SUFFIX() { return getConfigString(PATH + "suffix"); }
            public static String SPRITES_FULL() { return getConfigString(PATH + "sprites.full"); }
            public static String SPRITES_HALF() { return getConfigString(PATH + "sprites.half"); }
            public static int LINE_WIDTH() { return getConfigInt(PATH + "line-width"); }
            public static int WRAP_AT() { return getConfigInt(PATH + "wrap-at"); }
        }

        public static class Symbols {
            private static final String PATH = "bars.symbols.";
            public static String PREFIX() { return getConfigString(PATH + "prefix"); }
            public static String SUFFIX() { return getConfigString(PATH + "suffix"); }
            public static String SYMBOL() { return getConfigString(PATH + "symbol"); }
            public static String COLOR_HIGH() { return getConfigString(PATH + "color.high"); }
            public static String COLOR_MID()  { return getConfigString(PATH + "color.mid"); }
            public static String COLOR_LOW()  { return getConfigString(PATH + "color.low"); }
        }

        public static class Bars {
            private static final String PATH = "bars.bars.";
            public static String PREFIX() { return getConfigString(PATH + "prefix"); }
            public static String SUFFIX() { return getConfigString(PATH + "suffix"); }
            public static String SPRITES_BAR() { return getConfigString(PATH + "sprites.bar"); }
            public static String SPRITES_BACKGROUND() { return getConfigString(PATH + "sprites.background"); }
            public static boolean WRAP_AFTER_NAME() { return getConfigBoolean(PATH + "wrap-after-name"); }
        }
    }
}

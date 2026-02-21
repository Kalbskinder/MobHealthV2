package net.kalbskinder.mobHealth.configuration;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class Config {
    private static FileConfiguration config;

    public Config(FileConfiguration config) {
        Config.config = config;
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

    protected static List<String> getConfigStringList(String key) {
        return config.getStringList(key);
    }

    public static class General {
        public static final boolean ALWAYS_SHOW_NAME = getConfigBoolean("general.name-always-shown");
    }

    public static class HealthBars {
        public static class Skyblock {
            private static final String PATH = "bars.skyblock";
            public static final String PREFIX = getConfigString(PATH + ".prefix");
            public static final String SUFFIX = getConfigString(PATH + ".suffix");
            public static final String COLOR_NAME = getConfigString(PATH + ".color.name");
            public static final String COLOR_HIGH = getConfigString(PATH + ".color.high");
            public static final String COLOR_MID = getConfigString(PATH + ".color.mid");
            public static final String COLOR_LOW = getConfigString(PATH + ".color.low");
            public static final boolean DISABLE_MOB_NAME = getConfigBoolean(PATH + ".disable-mob-name");
        }
    }


}

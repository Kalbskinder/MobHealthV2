package net.kalbskinder.mobHealth.util;

import net.kalbskinder.mobHealth.configuration.Config;

public class HealthColorUtil {
    public String getSkyblockHealthColor(double health, double maxHealth) {
        double percentage = (health / maxHealth) * 100.0;
        if (percentage > 75.0) {
            return Config.HealthBars.Skyblock.COLOR_HIGH();
        } else if (percentage >= 25.0) {
            return Config.HealthBars.Skyblock.COLOR_MID();
        } else {
            return Config.HealthBars.Skyblock.COLOR_LOW();
        }
    }
}

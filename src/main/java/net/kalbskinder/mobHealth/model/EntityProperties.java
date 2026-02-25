package net.kalbskinder.mobHealth.model;

public record EntityProperties (
        String mobName,
        String nameColor,
        double health,
        double maxHealth,
        String levelPrefix,
        boolean levelPrefixEnabled,
        boolean disableMobName

) {}

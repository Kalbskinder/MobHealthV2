package net.kalbskinder.mobHealth.model;

public record EntityProperties (
        String mobName,
        double health,
        double maxHealth,
        String levelPrefix,
        boolean levelPrefixEnabled

) {}

package com.progiple.satellitedev.lightkits.configs;

import com.progiple.satellitedev.lightkits.LightKits;
import com.progiple.satellitedev.lightkits.kits.Kit;
import lombok.experimental.UtilityClass;
import org.novasparkle.lunaspring.API.configuration.Configuration;

@UtilityClass
public class PlayersConfig {
    private final Configuration config;
    static {
        config = new Configuration(LightKits.getINSTANCE().getDataFolder(), "playerTTL");
    }

    public long getLastClaimed(String playerName, Kit kit) {
        return config.getLong("players." + playerName + "." + kit.id());
    }

    public void setLastClaimed(String playerName, Kit kit) {
        config.setLong("players." + playerName + "." + kit.id(), System.currentTimeMillis());
        config.save();
    }

    public void resetLastClaimed(String playerName, Kit kit) {
        config.set("players." + playerName + "." + kit.id(), null);
        config.save();
    }
}

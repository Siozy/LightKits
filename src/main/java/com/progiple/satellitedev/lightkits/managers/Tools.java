package com.progiple.satellitedev.lightkits.managers;

import com.progiple.satellitedev.lightkits.configs.Config;
import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class Tools {
    private final Map<String, String> literators = new HashMap<>();

    public String timeFormer(long diff, boolean hasPerm) {
        if (!hasPerm) return literators.get("NOPERM");

        long allSeconds = diff / 1000;

        if (allSeconds <= 0) {
            return literators.get("NOTEXISTS");
        }

        int days = (int) (allSeconds / 86400);
        allSeconds -= days * 86400L;

        int hours = (int) (allSeconds / 3600);
        allSeconds -= hours * 3600L;

        int minutes = (int) (allSeconds / 60);
        allSeconds -= minutes * 60L;

        String form = "";
        if (days > 0) form += days + literators.get("DAYS");
        if (hours > 0) form += hours + literators.get("HOURS");
        if (minutes > 0) form += minutes + literators.get("MINUTES");
        if (allSeconds > 0) form += allSeconds + literators.get("SECONDS");
        return form;
    }

    public String timeFormer(long diff) {

        long allSeconds = diff / 1000;

        int days = (int) (allSeconds / 86400);
        allSeconds -= days * 86400L;

        int hours = (int) (allSeconds / 3600);
        allSeconds -= hours * 3600L;

        int minutes = (int) (allSeconds / 60);
        allSeconds -= minutes * 60L;

        String form = "";
        if (days > 0) form += days + literators.get("DAYS");
        if (hours > 0) form += hours + literators.get("HOURS");
        if (minutes > 0) form += minutes + literators.get("MINUTES");
        if (allSeconds > 0) form += allSeconds + literators.get("SECONDS");
        return form;
    }

    public void reloadLiterators() {
        ConfigurationSection format = Config.getSection("messages.cooldowns");

        literators.clear();
        if (format == null) return;
        format.getKeys(false).forEach(k -> literators.put(k.toUpperCase(), format.getString(k, "")));
    }
}

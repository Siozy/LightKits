package com.progiple.satellitedev.lightkits.configs;

import com.progiple.satellitedev.lightkits.LightKits;
import com.progiple.satellitedev.lightkits.managers.Tools;
import lombok.experimental.UtilityClass;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.novasparkle.lunaspring.API.configuration.IConfig;

import java.util.List;

@UtilityClass
public class Config {
    private final IConfig config;
    static {
        config = new IConfig(LightKits.getINSTANCE());
        Tools.reloadLiterators();
    }

    public void reload() {
        config.reload(LightKits.getINSTANCE());
        Tools.reloadLiterators();
    }

    public String getString(String path, String defaultArg) {
        return config.self().getString(path, defaultArg);
    }

    public List<Integer> getIntList(String path) { return config.getIntList(path); }

    public List<String> getList(String path) {
        return config.getStringList(path);
    }

    public static void sendMessage(CommandSender sender, String id, String... replacements) {
        config.sendMessage(sender, id, replacements);
    }

    public ConfigurationSection getSection(String path) {
        return config.getSection(path);
    }
}

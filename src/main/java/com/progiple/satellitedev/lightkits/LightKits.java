package com.progiple.satellitedev.lightkits;

import com.progiple.satellitedev.lightkits.managers.KitManager;
import lombok.Getter;
import org.novasparkle.lunaspring.API.commands.CommandInitializer;
import org.novasparkle.lunaspring.API.util.service.managers.TaskManager;
import org.novasparkle.lunaspring.LunaPlugin;

@SuppressWarnings("all")
public final class LightKits extends LunaPlugin {
    @Getter private static LightKits INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        super.onEnable();

        if (INSTANCE.getDataFolder() == null || !INSTANCE.getDataFolder().exists())
            loadFile("kits/example.yml");
        saveDefaultConfig();

        this.loadFiles("menus/main.yml", "menus/view.yml", "playerTTL.yml");
        KitManager.load();

        CommandInitializer.initialize(this, "#.commands");
    }

    @Override
    public void onDisable() {
        TaskManager.stopAll();
    }
}
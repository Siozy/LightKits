package com.progiple.satellitedev.lightkits.managers;

import com.progiple.satellitedev.lightkits.LightKits;
import com.progiple.satellitedev.lightkits.kits.Kit;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.UtilityClass;
import org.novasparkle.lunaspring.API.util.utilities.Utils;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

@UtilityClass @Accessors(fluent = true)
public class KitManager {
    @Getter
    private final Set<Kit> kits = new HashSet<>();

    public Kit getKit(String id) {
        return Utils.find(kits, k -> k.id().equals(id)).orElse(null);
    }

    public void register(Kit kit) {
        kits.add(kit);
    }

    public void load() {
        File dir = new File(LightKits.getINSTANCE().getDataFolder(), "kits/");

        kits.clear();
        if (!dir.exists()) return;

        File[] files = dir.listFiles();
        if (files == null) return;

        for (File file : files) {
            register(new Kit(file));
        }
    }

    public Kit create(String id) {
        if (getKit(id) != null) return null;

        Kit kit = new Kit(id);
        register(kit);
        return kit;
    }

    public boolean remove(String id) {
        Kit kit = getKit(id);
        if (kit == null) return false;

        return kits.remove(kit) && kit.remove();
    }
}

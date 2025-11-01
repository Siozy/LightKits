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
    @Getter private final Set<Kit> kits = new HashSet<>();
    @Getter private int maxPage = 0;

    public Kit getKit(String id) {
        return Utils.find(kits, k -> k.id().equals(id)).orElse(null);
    }

    public void register(Kit kit) {
        if (kit.visuals().page() > maxPage) maxPage = kit.visuals().page();
        kits.add(kit);
    }

    public void unregister(Kit kit) {
        kits.remove(kit);
        maxPage = kits
                .stream()
                .map(k -> k.visuals().page())
                .max(Integer::compareTo)
                .orElse(0);
    }

    public void load() {
        File dir = new File(LightKits.getINSTANCE().getDataFolder(), "kits/");

        kits.clear();
        if (!dir.exists()) return;

        File[] files = dir.listFiles();
        if (files == null) return;

        for (File file : files) {
            Kit kit = new Kit(file);
            register(kit);
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

        unregister(kit);
        return kit.remove();
    }
}

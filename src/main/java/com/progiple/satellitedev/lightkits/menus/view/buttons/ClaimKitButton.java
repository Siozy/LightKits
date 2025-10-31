package com.progiple.satellitedev.lightkits.menus.view.buttons;

import com.progiple.satellitedev.lightkits.configs.Config;
import com.progiple.satellitedev.lightkits.kits.Kit;
import com.progiple.satellitedev.lightkits.managers.Tools;
import com.progiple.satellitedev.lightkits.menus.InfoItem;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.novasparkle.lunaspring.self.configuration.Message;

public class ClaimKitButton extends InfoItem {
    public ClaimKitButton(@NotNull ConfigurationSection section, Kit kit) {
        super(section, section.getInt("slot"));
        this.kit = kit;
    }

    @Override
    public Item onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (!player.hasPermission("lightkits.bypass") && !kit.enabled()) {
            Config.sendMessage(player, "kitDisabled",
                    "name-%-" + kit.name(),
                    "id-%-" + kit.id());
            return this;
        }

        if (!kit.hasPermission(player)) {
            Config.sendMessage(player, "noPermission");
            return this;
        }

        if (kit.hasCooldown(player)) {
            String former = Tools.timeFormer(kit.getExpiredTime(player) - System.currentTimeMillis(), true);
            Config.sendMessage(player, "inCooldown",
                    "name-%-" + kit.name(),
                    "id-%-" + kit.id(),
                    "time-%-" + former);
            return this;
        }

        kit.claim(player);
        Config.sendMessage(player, "claim", "name-%-" + kit.name(), "id-%-" + kit.id());
        return this;
    }
}

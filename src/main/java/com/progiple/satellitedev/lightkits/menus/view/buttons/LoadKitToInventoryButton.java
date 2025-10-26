package com.progiple.satellitedev.lightkits.menus.view.buttons;

import com.progiple.satellitedev.lightkits.kits.Kit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.novasparkle.lunaspring.API.menus.items.Item;

public class LoadKitToInventoryButton extends Item {
    private final Kit kit;
    public LoadKitToInventoryButton(ConfigurationSection section, Kit kit) {
        super(section, section.getInt("slot"));
        this.kit = kit;
    }

    @Override
    public Item onClick(InventoryClickEvent e) {
        if (!e.getClick().isShiftClick()) return this;

        Player player = (Player) e.getWhoClicked();
        kit.downloadItems(player.getInventory());
        return this;
    }
}

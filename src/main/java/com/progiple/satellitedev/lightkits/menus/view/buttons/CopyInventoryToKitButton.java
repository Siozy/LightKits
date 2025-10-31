package com.progiple.satellitedev.lightkits.menus.view.buttons;

import com.progiple.satellitedev.lightkits.kits.Kit;
import com.progiple.satellitedev.lightkits.menus.view.ViewMenu;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.menus.items.Item;

public class CopyInventoryToKitButton extends Item {
    public CopyInventoryToKitButton(@NotNull ConfigurationSection section) {
        super(section, section.getInt("slot"));
    }

    @Override
    public Item onClick(InventoryClickEvent e) {
        if (!e.getClick().isShiftClick()) return this;

        Player player = (Player) e.getWhoClicked();
        if (this.getMenu() instanceof ViewMenu viewMenu) {
            Kit kit = viewMenu.getKit();
            kit.uploadItems(player.getInventory());

            viewMenu.rePasteItems();
        }
        return this;
    }
}

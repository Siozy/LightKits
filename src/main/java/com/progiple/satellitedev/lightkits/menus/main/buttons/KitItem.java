package com.progiple.satellitedev.lightkits.menus.main.buttons;

import com.progiple.satellitedev.lightkits.kits.Kit;
import com.progiple.satellitedev.lightkits.menus.InfoItem;
import com.progiple.satellitedev.lightkits.menus.itemEditor.EditItemsMenu;
import com.progiple.satellitedev.lightkits.menus.view.ViewMenu;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.menus.MenuManager;
import org.novasparkle.lunaspring.API.menus.items.Item;

public class KitItem extends InfoItem {
    public KitItem(@NotNull ConfigurationSection section, Kit kit) {
        super(section, kit.visuals().slot());
        this.kit = kit;
        this.setDisplayName(this.getDisplayName(), "name-%-" + kit.name());
    }

    @Override
    public Item onClick(InventoryClickEvent e) {
        e.setCancelled(true);

        Player player = (Player) e.getWhoClicked();
        if (e.getClick().isRightClick()) {
            EditItemsMenu editItemsMenu = MenuManager.getActiveMenus(EditItemsMenu.class, true)
                    .filter(m -> m.getKit() == kit)
                    .findFirst()
                    .orElse(null);
            if (editItemsMenu == null) editItemsMenu = new EditItemsMenu(player, kit);
            else editItemsMenu = (EditItemsMenu) editItemsMenu.copy(player);

            MenuManager.openInventory(editItemsMenu);
            return this;
        }

        ViewMenu viewMenu = new ViewMenu(player, this.kit);
        MenuManager.openInventory(viewMenu);
        return this;
    }

    @Override
    public KitItem setMaterial(@NotNull Material material) {
        super.setMaterial(material);
        return this;
    }
}

package com.progiple.satellitedev.lightkits.menus.itemEditor;

import com.progiple.satellitedev.lightkits.LightKits;
import com.progiple.satellitedev.lightkits.configs.Config;
import com.progiple.satellitedev.lightkits.kits.Kit;
import com.progiple.satellitedev.lightkits.menus.IKitMenu;
import com.progiple.satellitedev.lightkits.menus.main.MainMenu;
import com.progiple.satellitedev.lightkits.menus.view.ViewMenu;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.menus.AMenu;
import org.novasparkle.lunaspring.API.menus.MenuManager;
import org.novasparkle.lunaspring.API.util.utilities.Utils;

@Getter
public class EditItemsMenu extends AMenu implements IKitMenu {
    private final Kit kit;
    public EditItemsMenu(@NotNull Player player, Kit kit) {
        super(player, Utils.applyReplacements(Config.getString("editor_menu_title", "&0"),
                "id-%-" + kit.id(),
                "name-%-" + kit.name()), (byte) 45);
        this.kit = kit;
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
        this.kit.downloadItems(this.getInventory());
    }

    @Override
    public void onClick(InventoryClickEvent e) {
    }

    @Override
    public void onDrag(InventoryDragEvent e) {
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();

        this.kit.uploadItems(this.getInventory());
        Config.sendMessage(player, "saveKit", "id-%-" + kit.id(), "name-%-" + kit.name());

        Bukkit.getScheduler().runTaskLater(LightKits.getINSTANCE(), () -> {
            MenuManager.openInventory(new MainMenu(player, this.kit.visuals().page()));
        }, 3L);
    }
}
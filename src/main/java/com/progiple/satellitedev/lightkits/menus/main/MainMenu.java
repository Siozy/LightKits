package com.progiple.satellitedev.lightkits.menus.main;

import com.progiple.satellitedev.lightkits.LightKits;
import com.progiple.satellitedev.lightkits.kits.Kit;
import com.progiple.satellitedev.lightkits.managers.KitManager;
import com.progiple.satellitedev.lightkits.menus.IKitMenu;
import com.progiple.satellitedev.lightkits.menus.Updater;
import com.progiple.satellitedev.lightkits.menus.main.buttons.KitItem;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.configuration.IConfig;
import org.novasparkle.lunaspring.API.menus.AMenu;
import org.novasparkle.lunaspring.API.menus.MoveIgnored;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;

import java.util.stream.Collectors;

@MoveIgnored
public class MainMenu extends AMenu {
    private final IConfig config;
    private final Updater updater;
    public MainMenu(@NotNull Player player) {
        super(player);
        this.config = new IConfig(LightKits.getINSTANCE().getDataFolder(), "menus/main");
        this.initialize(ColorManager.color(this.config.getString("title")),
                (byte) (this.config.getInt("rows") * 9),
                this.config.getSection("items.decorations"),
                true);
        this.updater = new Updater(this);
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
        ConfigurationSection section = this.config.getSection("items.clickable");

        ConfigurationSection closeSection = section.getConfigurationSection("close");
        if (closeSection != null) {
            this.addItems(false, new Item(closeSection, closeSection.getInt("slot")) {
                @Override
                public Item onClick(InventoryClickEvent e) {
                    e.getWhoClicked().closeInventory();
                    return this;
                }
            });
        }

        ConfigurationSection itemSection = section.getConfigurationSection("kitItem");
        if (itemSection != null) this.addItems(KitManager.kits()
                .stream()
                .filter(Kit::visible)
                .map(k -> new KitItem(itemSection, k).setMaterial(k.visuals().material()))
                .collect(Collectors.toSet()), false);
        this.insertAll();

        this.updater.runTaskLaterAsynchronously(LightKits.getINSTANCE(), 2L);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        this.itemClick(event);
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        updater.cancel();
    }
}

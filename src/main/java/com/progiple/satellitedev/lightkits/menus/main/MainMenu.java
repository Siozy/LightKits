package com.progiple.satellitedev.lightkits.menus.main;

import com.progiple.satellitedev.lightkits.LightKits;
import com.progiple.satellitedev.lightkits.kits.Kit;
import com.progiple.satellitedev.lightkits.managers.KitManager;
import com.progiple.satellitedev.lightkits.menus.IKitMenu;
import com.progiple.satellitedev.lightkits.menus.InfoItem;
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
import org.novasparkle.lunaspring.API.menus.PageMenu;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.novasparkle.lunaspring.API.menus.items.SwitchItem;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;

import java.util.stream.Collectors;

@MoveIgnored
public class MainMenu extends AMenu {
    private final IConfig config;
    private final Updater updater;
    private final int page;
    public MainMenu(@NotNull Player player, int page) {
        super(player);
        this.page = page;
        this.config = new IConfig(LightKits.getINSTANCE().getDataFolder(), "menus/main");

        this.initialize(
                ColorManager.color(this.config.getString("title")
                        .replace("[page]", String.valueOf(this.page))
                        .replace("[maxPage]", String.valueOf(KitManager.maxPage()))),
                (byte) (this.config.getInt("rows") * 9),
                this.config.getSection("items.decorations"),
                true);
        this.updater = new Updater(this);
    }

    public MainMenu(@NotNull Player player) {
        this(player, 1);
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
                .filter(k -> k.visible() && k.visuals().page() == this.page)
                .map(k -> new KitItem(itemSection, k).setMaterial(k.visuals().material()))
                .collect(Collectors.toSet()), false);

        ConfigurationSection nextSection = section.getConfigurationSection("nextPage");
        if (nextSection != null && this.page < KitManager.maxPage()) {
            this.addItems(false, new SwitchItem(
                    nextSection,
                    nextSection.getInt("slot"),
                    p -> new MainMenu(p, this.page + 1)));
        }

        ConfigurationSection prevSection = section.getConfigurationSection("prevPage");
        if (prevSection != null && this.page > 1 && KitManager
                .kits()
                .stream()
                .anyMatch(k -> k.visible() && k.visuals().page() < this.page)) {
            this.addItems(false, new SwitchItem(
                    prevSection,
                    prevSection.getInt("slot"),
                    p -> new MainMenu(p, this.page - 1)));
        }

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

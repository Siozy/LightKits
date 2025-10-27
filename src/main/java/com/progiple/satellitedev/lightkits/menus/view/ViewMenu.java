package com.progiple.satellitedev.lightkits.menus.view;

import com.progiple.satellitedev.lightkits.LightKits;
import com.progiple.satellitedev.lightkits.kits.Kit;
import com.progiple.satellitedev.lightkits.menus.IKitMenu;
import com.progiple.satellitedev.lightkits.menus.Updater;
import com.progiple.satellitedev.lightkits.menus.main.MainMenu;
import com.progiple.satellitedev.lightkits.menus.view.buttons.ClaimKitButton;
import com.progiple.satellitedev.lightkits.menus.view.buttons.CopyInventoryToKitButton;
import com.progiple.satellitedev.lightkits.menus.view.buttons.LoadKitToInventoryButton;
import com.progiple.satellitedev.lightkits.menus.view.buttons.SwitchStatusKitButton;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.configuration.IConfig;
import org.novasparkle.lunaspring.API.menus.AMenu;
import org.novasparkle.lunaspring.API.menus.MoveIgnored;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.novasparkle.lunaspring.API.menus.items.SwitchItem;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;

@MoveIgnored @Getter
public class ViewMenu extends AMenu implements IKitMenu {
    private final IConfig config;
    private final Kit kit;
    private final Updater updater;
    public ViewMenu(@NotNull Player player, Kit kit) {
        super(player);
        this.kit = kit;
        this.config = new IConfig(LightKits.getINSTANCE().getDataFolder(), "menus/view");
        this.initialize(ColorManager.color(this.config.getString("title").replace("[name]", this.kit.name()),
                (byte) (this.config.getInt("rows") * 9),
                this.config.getSection("items.decorations"),
                true);
        this.updater = new Updater(this);
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {
        this.rePasteItems();
        this.loadButtons();
        this.updater.runTaskLaterAsynchronously(LightKits.getINSTANCE(), 2L);
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
        super.onClick(event);
    }

    public void rePasteItems() {
        boolean hasDeco = false;
        if (this.getDecoration() != null) {
            hasDeco = true;
            this.getDecoration().insert(this.getInventory());
        }

        ItemStack[] itemStacks = this.kit.items();
        for (int i = 0; i < itemStacks.length; i++) {
            int finalI = i;
            if (this.getItemList()
                    .stream()
                    .anyMatch(item -> item.getSlot() == finalI)) continue;

            ItemStack item = itemStacks[i];
            if (item == null && hasDeco) continue;

            this.getInventory().setItem(i, item);
        }
    }

    private void loadButtons() {
        ConfigurationSection section = this.config.getSection("items.clickable");
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            ConfigurationSection itemSection = section.getConfigurationSection(key);
            if (itemSection == null) continue;

            Item item = switch (key) {
                case "enableButton" -> {
                    if (getPlayer().hasPermission("lightkits.switch.*"))
                        yield new SwitchStatusKitButton(itemSection, kit);
                    yield null;
                }
                case "claim" -> new ClaimKitButton(itemSection, kit);
                case "copyFromKit" -> {
                    if (getPlayer().hasPermission("lightkits.copy"))
                        yield new LoadKitToInventoryButton(itemSection, kit);
                    yield null;
                }
                case "copyToKit" -> {
                    if (getPlayer().hasPermission("lightkits.copy"))
                        yield new CopyInventoryToKitButton(itemSection);
                    yield null;
                }
                case "back" -> new SwitchItem(itemSection, itemSection.getInt("slot"), MainMenu::new);
                default -> null;
            };
            if (item != null) this.addItems(false, item);
        }
        this.insertAll();
    }

    @Override
    public void onClose(InventoryCloseEvent e) {
        updater.cancel();
    }
}

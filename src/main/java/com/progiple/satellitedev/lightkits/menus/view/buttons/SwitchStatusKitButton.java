package com.progiple.satellitedev.lightkits.menus.view.buttons;

import com.progiple.satellitedev.lightkits.kits.Kit;
import com.progiple.satellitedev.lightkits.menus.InfoItem;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.menus.ItemListMenu;
import org.novasparkle.lunaspring.API.menus.items.Item;

public class SwitchStatusKitButton extends InfoItem {
    private final Kit kit;
    private final ConfigurationSection section;
    public SwitchStatusKitButton(@NotNull ConfigurationSection section, Kit kit) {
        super(section, section.getInt("slot"));
        this.section = section;
        this.kit = kit;

        this.loadStatus();
    }

    @Override
    public Item onClick(InventoryClickEvent e) {
        this.kit.switchStatus(!kit.enabled());
        this.loadStatus();
        return this;
    }

    private void loadStatus() {
        if (this.kit.enabled()) {
            this.setAll(this.section);
        }
        else {
            ConfigurationSection disableSection = this.section.getConfigurationSection("disableButton");
            if (disableSection != null) this.setAll(disableSection);
        }

        ItemListMenu menu = this.getMenu() != null ? this.getMenu() : null;
        if (menu != null) menu.addItems(true, this);
    }
}

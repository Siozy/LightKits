package com.progiple.satellitedev.lightkits.menus;

import com.progiple.satellitedev.lightkits.configs.Config;
import com.progiple.satellitedev.lightkits.kits.Kit;
import com.progiple.satellitedev.lightkits.managers.Tools;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.novasparkle.lunaspring.API.menus.ItemListMenu;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.novasparkle.lunaspring.API.util.utilities.Utils;

import java.util.ArrayList;

public abstract class InfoItem extends Item {
    protected Player player;
    protected Kit kit;
    public InfoItem(@NotNull ConfigurationSection section, int slot) {
        super(section, slot);
    }

    public Item tick() {
        ItemListMenu menu = this.getMenu();
        if (player == null) {
            if (menu == null) return this;
            player = menu.getPlayer();
        }

        if (kit == null) {
            if (menu instanceof IKitMenu kitMenu) {
                kit = kitMenu.getKit();
            }
            else return this;
        }

        long diff = kit.getExpiredTime(player) - System.currentTimeMillis();
        boolean hasPermission = kit.enabled() && kit.hasPermission(player);
        this.setLore(new ArrayList<>(this.getDefaultLore()),
                "name-%-" + kit.name(),
                "cooldown-%-" + Tools.timeFormer(diff, hasPermission),
                "claimCooldown-%-" + Tools.timeFormer(kit.cooldown() * 1000),
                "enabled-%-" + (kit.enabled() ? Config.getString("messages.switch.enabled", "Включено") : Config.getString("messages.switch.disabled", "Выключено")),
                "loreLine-%-" + kit.visuals().loreLine());
        this.replaceLore(l -> Utils.setPlaceholders(player, l));

        return this;
    }
}

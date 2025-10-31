package com.progiple.satellitedev.lightkits.menus.main;

import com.progiple.satellitedev.lightkits.configs.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.novasparkle.lunaspring.API.menus.PageMenu;

import java.util.List;

public class MainPageMenu extends PageMenu {
    private final List<Integer> itemsOrder = Config.getIntList("page_order");
    public MainPageMenu(Player player) {
        super(player, itemsOrder.get(0));
    }

    @Override
    public void reloadPage(int i) {

    }

    @Override
    public void onOpen(InventoryOpenEvent inventoryOpenEvent) {

    }
}

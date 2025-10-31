package com.progiple.satellitedev.lightkits.menus;

import lombok.SneakyThrows;
import org.novasparkle.lunaspring.API.menus.ItemListMenu;
import org.novasparkle.lunaspring.API.menus.items.Item;
import org.novasparkle.lunaspring.API.util.utilities.LunaTask;

import java.util.ArrayList;
import java.util.List;

public class Updater extends LunaTask {
    private final ItemListMenu menu;
    public Updater(ItemListMenu mainMenu) {
        super(0);
        this.menu = mainMenu;
    }

    @Override @SneakyThrows
    @SuppressWarnings("all")
    public void start() {
        while (true) {
            if (this.isCancelled() || menu.getInventory().getViewers().isEmpty()) return;

            List<Item> infoItems = this.menu.getItemList()
                    .stream()
                    .filter(i -> i instanceof InfoItem)
                    .map(i -> ((InfoItem) i).tick())
                    .toList();
            menu.getItemList()
                    .removeIf(i -> infoItems
                            .stream()
                            .anyMatch(j -> j.getSlot() == i.getSlot()));
            menu.addItems(infoItems, true);

            Thread.sleep(1000L);
        }
    }
}

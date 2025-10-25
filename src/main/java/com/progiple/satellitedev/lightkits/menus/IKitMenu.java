package com.progiple.satellitedev.lightkits.menus;

import com.progiple.satellitedev.lightkits.kits.Kit;
import org.novasparkle.lunaspring.API.menus.ItemListMenu;

public interface IKitMenu extends ItemListMenu {
    Kit getKit();
}

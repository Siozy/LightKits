package com.progiple.satellitedev.lightkits.commands;

import com.progiple.satellitedev.lightkits.menus.main.MainMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.API.commands.Invocation;
import org.novasparkle.lunaspring.API.commands.annotations.Flags;
import org.novasparkle.lunaspring.API.commands.annotations.ZeroArgCommand;
import org.novasparkle.lunaspring.API.commands.processor.NoArgCommand;
import org.novasparkle.lunaspring.API.menus.MenuManager;

@ZeroArgCommand("lightkits")
@Flags(NoArgCommand.AccessFlag.PLAYER_ONLY)
public class KitCommand implements Invocation {
    @Override
    public void invoke(CommandSender sender, String[] strings) {
        Player player = (Player) sender;
        MenuManager.openInventory(new MainMenu(player));
    }
}

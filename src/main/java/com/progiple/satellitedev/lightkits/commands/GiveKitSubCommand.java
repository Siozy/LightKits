package com.progiple.satellitedev.lightkits.commands;

import com.progiple.satellitedev.lightkits.configs.Config;
import com.progiple.satellitedev.lightkits.kits.Kit;
import com.progiple.satellitedev.lightkits.managers.KitManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.novasparkle.lunaspring.API.commands.LunaExecutor;
import org.novasparkle.lunaspring.API.commands.annotations.Args;
import org.novasparkle.lunaspring.API.commands.annotations.Permissions;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;
import org.novasparkle.lunaspring.API.util.utilities.Utils;

import java.util.List;
import java.util.stream.Collectors;

@SubCommand(appliedCommand = "lightkits", commandIdentifiers = "give")
@Permissions("lightkits.give")
@Args(min = 2, max = Integer.MAX_VALUE)
public class GiveKitSubCommand implements LunaExecutor {
    @Override
    public void invoke(CommandSender sender, String[] strings) {
        Kit kit = KitManager.getKit(strings[1]);
        if (kit == null) {
            Config.sendMessage(sender, "kitNotExists", "id-%-" + strings[1]);
            return;
        }

        Player target = strings.length == 2 ? (sender instanceof Player p ? p : null) : Bukkit.getPlayer(strings[2]);
        if (target == null) {
            Config.sendMessage(sender, "playerNotExists");
            return;
        }

        kit.give(target);
        Config.sendMessage(sender, "give",
                "name-%-" + kit.name(),
                "player-%-" + target.getName());
        Config.sendMessage(target, "onGet", "name-%-" + kit.name());
    }

    @Override
    public List<String> tabComplete(CommandSender sender, List<String> list) {
        return list.size() == 1 ? Utils.tabCompleterFiltering(
                KitManager.kits().stream().map(Kit::id).collect(Collectors.toSet()),
                list.get(0)) :
                list.size() == 2 ? Utils.getPlayerNicks(list.get(1)) : null;
    }
}

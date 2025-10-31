package com.progiple.satellitedev.lightkits.commands;

import com.progiple.satellitedev.lightkits.configs.Config;
import com.progiple.satellitedev.lightkits.configs.PlayersConfig;
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

@SubCommand(appliedCommand = "lightkits", commandIdentifiers = "reset")
@Permissions("lightkits.reset")
@Args(min = 2, max = Integer.MAX_VALUE)
public class ResetKitSubCommand implements LunaExecutor {
    // kit reset kit player

    @Override
    public void invoke(CommandSender sender, String[] strings) {
        Kit kit = KitManager.getKit(strings[1]);
        if (kit == null) {
            Config.sendMessage(sender, "kitNotExists", "id-%-" + strings[1]);
            return;
        }

        String target = strings.length == 2 ? (sender instanceof Player ? sender.getName() : null) : strings[2];
        if (target == null) {
            System.out.println("Команду нельзя выполнить от лица консоли без аргумента игрока!");
            return;
        }

        PlayersConfig.resetLastClaimed(target, kit);
        Config.sendMessage(sender, "reset",
                "name-%-" + kit.name(),
                "player-%-" + target);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, List<String> list) {
        return list.size() == 1 ? Utils.tabCompleterFiltering(
                KitManager.kits().stream().map(Kit::id).collect(Collectors.toSet()),
                list.get(0)) :
                list.size() == 2 ? Utils.getPlayerNicks(list.get(1)) : null;
    }
}

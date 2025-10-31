package com.progiple.satellitedev.lightkits.commands;

import com.progiple.satellitedev.lightkits.configs.Config;
import com.progiple.satellitedev.lightkits.kits.Kit;
import com.progiple.satellitedev.lightkits.managers.KitManager;
import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.commands.LunaExecutor;
import org.novasparkle.lunaspring.API.commands.annotations.Args;
import org.novasparkle.lunaspring.API.commands.annotations.Permissions;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;
import org.novasparkle.lunaspring.API.commands.annotations.TabCompleteIgnore;
import org.novasparkle.lunaspring.API.util.utilities.Utils;

import java.util.List;
import java.util.stream.Collectors;

@SubCommand(appliedCommand = "lightkits", commandIdentifiers = {"remove", "delete", "rem", "del"})
@TabCompleteIgnore({"delete", "rem", "del"})
@Permissions("lightkits.remove")
@Args(min = 2, max = Integer.MAX_VALUE)
public class RemoveKitSubCommand implements LunaExecutor {
    @Override
    public void invoke(CommandSender sender, String[] strings) {
        String id = strings[1];
        if (KitManager.remove(id)) {
            Config.sendMessage(sender, "remove", "id-%-" + id);
        }
        else {
            Config.sendMessage(sender, "kitNotExists", "id-%-" + id);
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, List<String> list) {
        return list.size() == 1 ? Utils.tabCompleterFiltering(
                KitManager.kits().stream().map(Kit::id).collect(Collectors.toSet()),
                list.get(0)
        ) : null;
    }
}

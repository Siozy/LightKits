package com.progiple.satellitedev.lightkits.commands;

import com.progiple.satellitedev.lightkits.configs.Config;
import com.progiple.satellitedev.lightkits.managers.KitManager;
import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.commands.LunaExecutor;
import org.novasparkle.lunaspring.API.commands.annotations.Args;
import org.novasparkle.lunaspring.API.commands.annotations.Permissions;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;

import java.util.List;

@SubCommand(appliedCommand = "lightkits", commandIdentifiers = "create")
@Permissions("lightkits.create")
@Args(min = 2, max = Integer.MAX_VALUE)
public class CreateKitSubCommand implements LunaExecutor {
    @Override
    public void invoke(CommandSender sender, String[] strings) {
        if (KitManager.create(strings[1]) == null) {
            Config.sendMessage(sender, "kitExistsNow", "id-%-" + strings[1]);
        }
        else {
            Config.sendMessage(sender, "create", "id-%-" + strings[1]);
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, List<String> list) {
        return list.size() == 1 ? List.of("[id]") : null;
    }
}

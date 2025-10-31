package com.progiple.satellitedev.lightkits.commands.status;

import com.progiple.satellitedev.lightkits.configs.Config;
import com.progiple.satellitedev.lightkits.kits.Kit;
import com.progiple.satellitedev.lightkits.managers.KitManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.novasparkle.lunaspring.API.commands.Invocation;

@RequiredArgsConstructor
public abstract class AbsSwitchSubCommand implements Invocation {
    private final boolean prime;

    @Override
    public void invoke(CommandSender sender, String[] strings) {
        for (Kit kit : KitManager.kits()) {
            kit.switchStatus(prime);
        }
        Config.sendMessage(sender, (prime ? "enable" : "disable") + "All");
    }
}

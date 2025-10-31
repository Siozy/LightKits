package com.progiple.satellitedev.lightkits.commands.status;

import org.novasparkle.lunaspring.API.commands.annotations.Permissions;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;

@SubCommand(appliedCommand = "lightkits", commandIdentifiers = "onAll")
@Permissions({"lightkits.switch.enable", "lightkits.switch.*"})
public class EnableAllSubCommand extends AbsSwitchSubCommand {
    public EnableAllSubCommand() {
        super(true);
    }
}

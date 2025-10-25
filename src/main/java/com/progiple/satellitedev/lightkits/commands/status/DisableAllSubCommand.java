package com.progiple.satellitedev.lightkits.commands.status;

import org.novasparkle.lunaspring.API.commands.annotations.Permissions;
import org.novasparkle.lunaspring.API.commands.annotations.SubCommand;

@SubCommand(appliedCommand = "lightkits", commandIdentifiers = "offAll")
@Permissions({"lightkits.switch.disable", "lightkits.switch.*"})
public class DisableAllSubCommand extends AbsSwitchSubCommand {
    public DisableAllSubCommand() {
        super(false);
    }
}


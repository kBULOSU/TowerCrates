package br.com.yiatzz.crates.commands;

import br.com.idea.api.shared.commands.CommandRestriction;
import br.com.idea.api.spigot.commands.CustomCommand;
import br.com.yiatzz.crates.commands.subcommands.GiveKeySubCommand;
import br.com.yiatzz.crates.commands.subcommands.SetHeadPosSubCommand;

public class CrateCommand extends CustomCommand {

    public CrateCommand() {
        super("crate", CommandRestriction.CONSOLE_AND_IN_GAME);

        registerSubCommand(new GiveKeySubCommand());
        registerSubCommand(new SetHeadPosSubCommand());
    }
}

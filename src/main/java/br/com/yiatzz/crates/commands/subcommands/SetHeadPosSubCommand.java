package br.com.yiatzz.crates.commands.subcommands;

import br.com.idea.api.shared.commands.Argument;
import br.com.idea.api.shared.commands.CommandRestriction;
import br.com.idea.api.spigot.commands.CustomCommand;
import br.com.idea.api.spigot.misc.message.Message;
import br.com.yiatzz.crates.CratesProvider;
import br.com.yiatzz.crates.crate.Crate;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetHeadPosSubCommand extends CustomCommand {

    public SetHeadPosSubCommand() {
        super("setheadpos", CommandRestriction.IN_GAME);

        registerArgument(new Argument("crate", "Nome da crate"));

        setPermission("crates.admin");
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        Crate crate = CratesProvider.Cache.Local.CRATES.provide().get(args[0]);
        if (crate == null) {
            Message.ERROR.send(player, "Crate inválida.");
            return;
        }

        CratesProvider.Repositories.LOCATIONS.provide().insertOrUpdate(crate.getName(), player.getLocation().clone().add(0.0, 1.0, 0.0));
        CratesProvider.Cache.Local.CRATES.provide().invalidate(crate.getName());

        Message.SUCCESS.send(player, "Localização da cabeça definida com sucesso.");
    }
}

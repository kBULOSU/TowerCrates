package br.com.yiatzz.crates.commands.subcommands;

import br.com.idea.api.shared.commands.Argument;
import br.com.idea.api.shared.commands.CommandRestriction;
import br.com.idea.api.shared.misc.utils.DefaultMessage;
import br.com.idea.api.spigot.commands.CustomCommand;
import br.com.idea.api.spigot.misc.customitem.CustomItem;
import br.com.idea.api.spigot.misc.customitem.CustomItemRegistry;
import br.com.idea.api.spigot.misc.message.Message;
import br.com.idea.api.spigot.misc.utils.InventoryUtils;
import br.com.yiatzz.crates.CratesProvider;
import br.com.yiatzz.crates.crate.Crate;
import br.com.yiatzz.crates.misc.customitem.KeyCustomItem;
import com.google.common.primitives.Ints;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveKeySubCommand extends CustomCommand {

    public GiveKeySubCommand() {
        super("givekey", CommandRestriction.CONSOLE_AND_IN_GAME, "darkey");

        registerArgument(new Argument("jogador", "Jogador pra dar a key."));
        registerArgument(new Argument("crate", "Nome da crate"));
        registerArgument(new Argument("quantia", "Quantia de keys"));

        setPermission("crates.admin");
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null || !target.isOnline()) {
            sender.sendMessage(DefaultMessage.PLAYER_NOT_FOUND.format(args[0]));
            return;
        }

        Crate crate = CratesProvider.Cache.Local.CRATES.provide().get(args[1]);
        if (crate == null) {
            Message.ERROR.send(sender, "Crate inválida.");
            return;
        }

        Integer amount = Ints.tryParse(args[2]);
        if (amount == null || amount <= 0) {
            Message.ERROR.send(sender, "Quantia inválida.");
            return;
        }

        CustomItem item = CustomItemRegistry.getItem(KeyCustomItem.KEY + "-" + crate.getName());
        if (item == null) {
            Message.ERROR.send(sender, "Item inválido.");
            return;
        }

        ItemStack itemStack = item.asItemStack(amount);
        if (!InventoryUtils.fits(target.getInventory(), itemStack)) {
            Message.ERROR.send(sender, "Usuário com o inventário cheio.");
            return;
        }

        target.getInventory().addItem(itemStack);

        Message.SUCCESS.send(sender, "Key(s) da crate " + crate.getDisplayName() + " enviada com sucesso!");
    }
}

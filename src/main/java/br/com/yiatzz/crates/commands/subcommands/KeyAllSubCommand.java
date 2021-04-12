package br.com.yiatzz.crates.commands.subcommands;

import br.com.idea.api.shared.commands.Argument;
import br.com.idea.api.shared.commands.CommandRestriction;
import br.com.idea.api.spigot.commands.CustomCommand;
import br.com.idea.api.spigot.misc.customitem.CustomItem;
import br.com.idea.api.spigot.misc.customitem.CustomItemRegistry;
import br.com.idea.api.spigot.misc.message.Message;
import br.com.idea.api.spigot.misc.utils.InventoryUtils;
import br.com.yiatzz.crates.CratesProvider;
import br.com.yiatzz.crates.crate.Crate;
import br.com.yiatzz.crates.keys.customitem.KeyCustomItem;
import com.google.common.primitives.Ints;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class KeyAllSubCommand extends CustomCommand {

    public KeyAllSubCommand() {
        super("keyall", CommandRestriction.CONSOLE_AND_IN_GAME);

        registerArgument(new Argument("crate", ""));
        registerArgument(new Argument("quantia", ""));

        setPermission("crates.admin");
    }

    @Override
    public void onCommand(CommandSender sender, String[] args) {
        Crate crate = CratesProvider.Cache.Local.CRATES.provide().get(args[0]);
        if (crate == null) {
            Message.ERROR.send(sender, "Crate inválida.");
            return;
        }

        Integer integer = Ints.tryParse(args[1]);
        if (integer == null || integer <= 0) {
            Message.ERROR.send(sender, "Quantia inválida.");
            return;
        }

        CustomItem item = CustomItemRegistry.getItem(KeyCustomItem.KEY + "-" + crate.getName());
        if (item == null) {
            Message.ERROR.send(sender, "Item inválido.");
            return;
        }

        ItemStack itemStack = item.asItemStack(integer);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!InventoryUtils.fits(onlinePlayer.getInventory(), itemStack)) {
                return;
            }

            onlinePlayer.getInventory().addItem(itemStack);
        }

        Message.SUCCESS.send(sender, "Key(s) enviadas com sucesso.");
    }
}

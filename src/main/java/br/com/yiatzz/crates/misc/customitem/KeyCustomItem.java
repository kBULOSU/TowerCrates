package br.com.yiatzz.crates.misc.customitem;

import br.com.idea.api.spigot.misc.customitem.CustomItem;
import br.com.idea.api.spigot.misc.message.Message;
import br.com.idea.api.spigot.misc.utils.CustomSound;
import br.com.idea.api.spigot.misc.utils.InventoryUtils;
import br.com.idea.api.spigot.misc.utils.ItemBuilder;
import br.com.yiatzz.crates.CratesPlugin;
import br.com.yiatzz.crates.CratesProvider;
import br.com.yiatzz.crates.crate.Crate;
import br.com.yiatzz.crates.crate.CrateItem;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.greenrobot.eventbus.Subscribe;

public class KeyCustomItem extends CustomItem {

    private static final int MAX_ITENS = 50;

    public static final String KEY = "key-custom-item";

    private final Crate crate;

    private final ItemBuilder itemBuilder;

    public KeyCustomItem(Crate crate) {
        super(KEY + "-" + crate.getName());

        this.crate = crate;

        this.itemBuilder = new ItemBuilder(Material.TRIPWIRE_HOOK)
                .name("&eKey de Crate")
                .lore("&7Crate: &f" + crate.getDisplayName());
    }

    @Override
    protected ItemBuilder getItemBuilder() {
        return itemBuilder;
    }

    @Override
    public String getDisplayName() {
        return "Key da Crate " + crate.getDisplayName();
    }

    @Subscribe
    public void on(PlayerInteractAtEntityEvent event) {
        Entity rightClicked = event.getRightClicked();
        if (!(rightClicked instanceof ArmorStand) || !rightClicked.hasMetadata("crate_name")) {
            return;
        }

        event.setCancelled(true);

        String crateName = rightClicked.getMetadata("crate_name").get(0).asString();

        Crate crate = CratesProvider.Cache.Local.CRATES.provide().get(crateName);
        if (crate == null) {
            return;
        }

        Player player = event.getPlayer();

        if (!this.crate.getName().equals(crate.getName())) {
            Message.ERROR.send(player, "Esta chave não abre esta crate.");
            return;
        }

        InventoryUtils.subtractOneOnHand(player);

        Bukkit.getScheduler().runTaskAsynchronously(CratesPlugin.getInstance(), () -> {
            player.getLocation().getWorld().spigot().playEffect(player.getLocation(),
                    Effect.WITCH_MAGIC, 1, 0, 0.5F, 0.1F, 0.5F, 1, 100, 15);
        });

        CustomSound.GOOD.play(player);

        CrateItem item = crate.getRandomItem();
        if (item.getCommand() != null && !item.getCommand().isEmpty()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), StringUtils.replace(item.getCommand(), "<player>", player.getName()));
            return;
        }

        ItemStack itemStack = item.asItemStack();
        if (!InventoryUtils.fits(player.getInventory(), itemStack)) {
            player.getWorld().dropItem(player.getLocation(), itemStack);
        } else {
            player.getInventory().addItem(itemStack);
        }
    }
}

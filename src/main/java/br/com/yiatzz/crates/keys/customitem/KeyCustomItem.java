package br.com.yiatzz.crates.keys.customitem;

import br.com.idea.api.shared.messages.MessageUtils;
import br.com.idea.api.spigot.misc.customitem.CustomItem;
import br.com.idea.api.spigot.misc.message.Message;
import br.com.idea.api.spigot.misc.utils.InventoryUtils;
import br.com.idea.api.spigot.misc.utils.ItemBuilder;
import br.com.yiatzz.crates.CratesPlugin;
import br.com.yiatzz.crates.CratesProvider;
import br.com.yiatzz.crates.crate.Crate;
import br.com.yiatzz.crates.crate.CrateItem;
import br.com.yiatzz.crates.keys.KeySection;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.greenrobot.eventbus.Subscribe;

import java.util.Arrays;

public class KeyCustomItem extends CustomItem {

    public static final String KEY = "key-custom-item";

    private final Crate crate;

    private final ItemBuilder itemBuilder;

    public KeyCustomItem(Crate crate, KeySection section) {
        super(KEY + "-" + crate.getName());

        this.crate = crate;

        this.itemBuilder = new ItemBuilder(Material.TRIPWIRE_HOOK)
                .name(StringUtils.replace(MessageUtils.translateColorCodes(section.getDisplayName()), "<crateName>", crate.getDisplayName()))
                .lore(Arrays.stream(section.getLore()).map(MessageUtils::translateColorCodes).toArray(String[]::new));
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
            player.playEffect(player.getLocation(), Effect.EXPLOSION_HUGE, 0);
        });

        Location location = crate.getHead().getLocation();

        Firework entity = (Firework) location.getWorld().spawnEntity(location.clone().add(0.0, 3.5, 0.0), EntityType.FIREWORK);
        FireworkMeta fireworkMeta = entity.getFireworkMeta();

        fireworkMeta.setPower(2);
        fireworkMeta.addEffect(
                FireworkEffect.builder()
                        .withColor(Color.FUCHSIA)
                        .withColor(Color.LIME)
                        .flicker(true)
                        .build()
        );

        entity.setFireworkMeta(fireworkMeta);

        Bukkit.getScheduler().runTaskLater(CratesPlugin.getInstance(), entity::detonate, 12);

        player.playSound(player.getLocation(), Sound.EXPLODE, 1F, 1F);

        CrateItem item = crate.getRandomItem();

        if (item.hasChat()) {
            player.sendMessage(MessageUtils.translateColorCodes(item.getChatMessage()));
        }

        if (item.hasTitle()) {
            player.sendTitle(MessageUtils.translateColorCodes(item.getTitle()), MessageUtils.translateColorCodes(item.getSubTitle()));
        }

        if (!item.getCommand().isEmpty()) {

            for (String command : item.getCommand()) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), StringUtils.replace(command, "<player>", player.getName()));
            }

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

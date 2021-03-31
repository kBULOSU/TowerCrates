package br.com.yiatzz.crates.listeners;

import br.com.idea.api.spigot.misc.customitem.CustomItemRegistry;
import br.com.yiatzz.crates.CratesProvider;
import br.com.yiatzz.crates.crate.Crate;
import br.com.yiatzz.crates.inventories.CratePreviewInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class InteractWithArmorStandListener implements Listener {

    @EventHandler
    public void on(PlayerInteractAtEntityEvent event) {
        if (!event.getRightClicked().hasMetadata("crate_name")) {
            return;
        }

        event.setCancelled(true);

        Crate crate = CratesProvider.Cache.Local.CRATES.provide().get(event.getRightClicked().getMetadata("crate_name").get(0).asString());
        if (crate == null) {
            return;
        }

        if (CustomItemRegistry.getByItemStack(event.getPlayer().getItemInHand()) != null) {
            return;
        }

        event.getPlayer().openInventory(new CratePreviewInventory(crate));
    }
}

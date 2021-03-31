package br.com.yiatzz.crates.task;

import br.com.idea.api.spigot.misc.utils.HeadTexture;
import br.com.yiatzz.crates.CratesPlugin;
import br.com.yiatzz.crates.CratesProvider;
import br.com.yiatzz.crates.crate.Crate;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.metadata.FixedMetadataValue;

public class CratesHeadsRotationTask implements Runnable {

    @Override
    public void run() {
        for (Crate crate : CratesProvider.Cache.Local.CRATES.provide().CACHE.values()) {
            Location location = CratesProvider.Cache.Local.CRATES.provide().getLocation(crate.getName());
            if (location == null) {
                continue;
            }

            if (!location.getChunk().isLoaded()) {
                location.getChunk().load();
            }

            ArmorStand head = crate.getHead();
            if (head == null || !head.isValid()) {
                ArmorStand entity = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

                entity.setGravity(false);
                entity.setCustomNameVisible(false);
                entity.setVisible(false);
                entity.setArms(false);

                entity.setHelmet(HeadTexture.getTempHead(crate.getHeadKey()));
                entity.setMetadata("crate_name", new FixedMetadataValue(CratesPlugin.getInstance(), crate.getName()));

                crate.setHead(entity);
                head = crate.getHead();
            }

            location.setYaw(location.getYaw() + 180);

            head.teleport(location);
        }
    }
}

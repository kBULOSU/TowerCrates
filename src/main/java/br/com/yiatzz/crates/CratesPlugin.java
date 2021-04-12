package br.com.yiatzz.crates;

import br.com.idea.api.shared.messages.MessageUtils;
import br.com.idea.api.spigot.commands.CommandRegistry;
import br.com.idea.api.spigot.misc.customitem.CustomItemRegistry;
import br.com.idea.api.spigot.misc.utils.HeadTexture;
import br.com.yiatzz.crates.commands.CrateCommand;
import br.com.yiatzz.crates.crate.Crate;
import br.com.yiatzz.crates.keys.KeySection;
import br.com.yiatzz.crates.keys.KeysConfigLoader;
import br.com.yiatzz.crates.keys.customitem.KeyCustomItem;
import br.com.yiatzz.crates.listeners.InteractWithArmorStandListener;
import br.com.yiatzz.crates.runnable.CratesHeadsRotationRunnable;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CratesPlugin extends JavaPlugin {

    @Getter
    private static CratesPlugin instance;

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void onEnable() {
        super.onEnable();

        instance = this;

        saveDefaultConfig();

        CratesProvider.prepare();

        CratesConfigLoader.load(getConfig());
        KeysConfigLoader.load(getConfig());

        HologramsAPI.getHolograms(this).forEach(Hologram::delete);

        for (Crate crate : CratesProvider.Cache.Local.CRATES.provide().CACHE.values()) {
            KeySection keySection = CratesProvider.Cache.Local.KEYS.provide().get(crate.getName());
            if (keySection == null) {
                System.out.println("A key " + crate.getName() + " tá errada.");
                continue;
            }

            CustomItemRegistry.registerCustomItem(new KeyCustomItem(crate, keySection));
        }

        CommandRegistry.registerCommand(new CrateCommand());

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new InteractWithArmorStandListener(), this);

        Bukkit.getScheduler().runTaskLater(this, () -> {

            for (World world : Bukkit.getWorlds()) {
                List<Entity> entities = world.getEntities();
                for (Entity entity : entities) {
                    if (entity.getType() != EntityType.ARMOR_STAND) {
                        continue;
                    }

                    ArmorStand armorStand = (ArmorStand) entity;
                    if (armorStand.getHelmet().getType() != Material.SKULL_ITEM) {
                        continue;
                    }

                    armorStand.remove();
                }
            }

            for (Crate crate : CratesProvider.Cache.Local.CRATES.provide().CACHE.values()) {
                ArmorStand head = crate.getHead();
                if (head == null) {
                    Location location = CratesProvider.Cache.Local.CRATES.provide().getLocation(crate.getName());
                    if (location == null) {
                        continue;
                    }

                    if (!location.getChunk().isLoaded()) {
                        location.getChunk().load();
                    }

                    location.getWorld().getNearbyEntities(location, 3.0, 3.0, 3.0)
                            .stream()
                            .filter(entity -> entity.getType() == EntityType.ARMOR_STAND)
                            .filter(entity -> entity.hasMetadata("crate_name"))
                            .filter(entity -> entity.getMetadata("crate_name").get(0).asString().equalsIgnoreCase(crate.getName()))
                            .findFirst()
                            .ifPresent(Entity::remove);

                    ArmorStand entity = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

                    entity.setGravity(false);
                    entity.setCustomNameVisible(false);
                    entity.setVisible(false);
                    entity.setArms(false);

                    entity.setHelmet(HeadTexture.getTempHead(crate.getHeadKey()));
                    entity.setMetadata("crate_name", new FixedMetadataValue(this, crate.getName()));

                    crate.setHead(entity);

                    if (crate.getHologram() == null) {
                        Hologram hologram = HologramsAPI.createHologram(this, location.clone().add(0.0, 5.4, 0.0));

                        for (String hologramLine : crate.getHologramLines()) {
                            hologram.appendTextLine(MessageUtils.translateColorCodes(hologramLine));
                        }

                        crate.setHologram(hologram);
                    }
                }
            }

            executorService.scheduleAtFixedRate(new CratesHeadsRotationRunnable(), 1, 1, TimeUnit.MILLISECONDS);

        }, 40L);

    }

    @Override
    public void onDisable() {
        super.onDisable();

        CratesProvider.Cache.Local.CRATES.provide().CACHE.values().forEach(crate -> {
            if (crate.getHologram() != null) {
                crate.getHologram().delete();
            }

            if (crate.getHead() != null) {
                crate.getHead().remove();
            }
        });

        HologramsAPI.getHolograms(this).forEach(Hologram::delete);

        Bukkit.getScheduler().cancelTasks(this);
    }
}

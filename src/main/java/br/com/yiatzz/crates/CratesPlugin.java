package br.com.yiatzz.crates;

import br.com.idea.api.spigot.commands.CommandRegistry;
import br.com.idea.api.spigot.misc.customitem.CustomItemRegistry;
import br.com.yiatzz.crates.commands.CrateCommand;
import br.com.yiatzz.crates.crate.Crate;
import br.com.yiatzz.crates.listeners.InteractWithArmorStandListener;
import br.com.yiatzz.crates.misc.customitem.KeyCustomItem;
import br.com.yiatzz.crates.task.CratesHeadsRotationTask;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class CratesPlugin extends JavaPlugin {

    @Getter
    private static CratesPlugin instance;

    @Override
    public void onEnable() {
        super.onEnable();

        instance = this;

        saveDefaultConfig();

        CratesProvider.prepare();

        CratesConfigLoader.load(getConfig());

        for (World world : Bukkit.getWorlds()) {
            world.spawnEntity(world.getSpawnLocation(), EntityType.LIGHTNING);

            List<Entity> entities = world.getEntities();
            for (Entity entity : entities) {
                if (entity instanceof ArmorStand) {
                    if (HologramsAPI.isHologramEntity(entity)) {
                        continue;
                    }

                    entity.remove();
                }
            }
        }

        Bukkit.getScheduler().runTaskTimer(this, new CratesHeadsRotationTask(), 20L, 20L);

        for (Crate crate : CratesProvider.Cache.Local.CRATES.provide().CACHE.values()) {
            CustomItemRegistry.registerCustomItem(new KeyCustomItem(crate));
        }

        CommandRegistry.registerCommand(new CrateCommand());

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new InteractWithArmorStandListener(), this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Bukkit.getScheduler().cancelTasks(this);
    }
}

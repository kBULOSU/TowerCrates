package br.com.yiatzz.crates.keys;

import br.com.yiatzz.crates.CratesProvider;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class KeysConfigLoader {

    public static void load(FileConfiguration config) {
        ConfigurationSection section = config.getConfigurationSection("Keys");
        if (section == null) {
            return;
        }

        for (String key : section.getKeys(false)) {
            String crate = section.getString(key + ".crate");
            String displayName = section.getString(key + ".displayName");
            String[] lore = section.getStringList(key + ".lore").toArray(new String[0]);

            KeySection keySection = new KeySection(crate, displayName, lore);

            CratesProvider.Cache.Local.KEYS.provide().put(keySection);
        }
    }
}

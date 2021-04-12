package br.com.yiatzz.crates;

import br.com.idea.api.spigot.misc.utils.ItemBuilder;
import br.com.yiatzz.crates.crate.Crate;
import br.com.yiatzz.crates.crate.CrateItem;
import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;

import java.util.LinkedList;
import java.util.List;

public class CratesConfigLoader {

    public static void load(FileConfiguration config) {
        ConfigurationSection boxes = config.getConfigurationSection("Crates");
        if (boxes == null) {
            return;
        }

        for (String key : boxes.getKeys(false)) {
            String headKey = boxes.getString(key + ".head-key");
            String name = boxes.getString(key + ".name");
            String displayName = boxes.getString(key + ".displayName");
            LinkedList<String> hologram = new LinkedList<>(boxes.getStringList(key + ".hologram"));

            LinkedList<CrateItem> boxItems = Lists.newLinkedList();

            ConfigurationSection items = boxes.getConfigurationSection(key + ".itens");
            for (String itemsKey : items.getKeys(false)) {
                String itemDisplayName = items.getString(itemsKey + ".displayName");
                double chance = items.getDouble(itemsKey + ".chance");
                List<String> command = items.getStringList(itemsKey + ".command");

                String chatMessage = items.getString(itemsKey + ".chat");
                String title = items.getString(itemsKey + ".title");
                String subTitle = items.getString(itemsKey + ".subtitle");

                ConfigurationSection itemStack = items.getConfigurationSection(itemsKey + ".itemStack");
                Material material = Material.getMaterial(itemStack.getInt("material"));
                short data = (short) itemStack.getInt("data");
                int amount = itemStack.getInt("amount");

                List<String> enchantments = itemStack.getStringList("enchantments");

                ItemBuilder itemBuilder = new ItemBuilder(material)
                        .durability(data)
                        .amount(amount);

                if (enchantments != null && !enchantments.isEmpty() && !StringUtils.isEmpty(enchantments.get(0))) {
                    enchantments.forEach(string -> {
                        String[] split = string.split(",");

                        if (split.length < 2) {
                            return;
                        }

                        Integer level = Ints.tryParse(split[1]);
                        if (level == null || level <= 0) {
                            return;
                        }

                        Enchantment byName = Enchantment.getByName(split[0]);
                        if (byName == null) {
                            return;
                        }

                        itemBuilder.enchantment(byName, level);
                    });
                }

                boxItems.add(new CrateItem(
                                Integer.parseInt(itemsKey),
                                itemBuilder,
                                itemDisplayName,
                                chance,
                                command,
                                chatMessage,
                                title,
                                subTitle
                        )
                );
            }

            Crate crate = new Crate(
                    name, displayName, headKey, boxItems, hologram
            );

            CratesProvider.Cache.Local.CRATES.provide().add(
                    name,
                    crate
            );
        }
    }
}

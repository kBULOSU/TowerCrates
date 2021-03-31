package br.com.yiatzz.crates.inventories;

import br.com.idea.api.shared.messages.MessageUtils;
import br.com.idea.api.spigot.inventory.CustomInventory;
import br.com.idea.api.spigot.misc.utils.ItemBuilder;
import br.com.yiatzz.crates.crate.Crate;
import br.com.yiatzz.crates.crate.CrateItem;
import org.bukkit.inventory.ItemStack;

public class CratePreviewInventory extends CustomInventory {

    public CratePreviewInventory(Crate crate) {
        super(9 * 6, "Crate " + MessageUtils.translateColorCodes(crate.getDisplayName()));

        int index = 10;

        for (CrateItem item : crate.getItems()) {
            if (item.asItemStack() == null) {
                continue;
            }

            if ((index + 1) % 9 == 0) {
                index += 2;
            }

            ItemStack icon = new ItemBuilder(item.asItemStack())
                    .lore(
                            "",
                            "&fChance: &b" + item.getWeight() + "%"
                    )
                    .make();

            setItem(index++, icon);
        }
    }
}

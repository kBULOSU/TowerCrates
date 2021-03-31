package br.com.yiatzz.crates.crate;

import br.com.idea.api.spigot.misc.utils.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.inventory.ItemStack;

@ToString
@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class CrateItem {

    private final int id;
    private final ItemBuilder displayItem;
    private final double weight;
    private final String command;

    public ItemStack asItemStack() {
        ItemBuilder clone = this.displayItem.clone();
        return this.asItemStack(clone.clone());
    }

    public final ItemStack asItemStack(int amount) {
        ItemBuilder clone = this.displayItem.clone();
        return this.asItemStack(clone.clone().amount(amount));
    }

    public final ItemStack asItemStack(ItemBuilder itemBuilder) {
        return itemBuilder.clone().make();
    }
}

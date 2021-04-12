package br.com.yiatzz.crates.crate;

import br.com.idea.api.shared.messages.MessageUtils;
import br.com.idea.api.spigot.misc.utils.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class CrateItem {

    private final int id;

    private final ItemBuilder displayItem;

    private final String displayName;

    private final double weight;

    private final List<String> command;
    private final String chatMessage;
    private final String title;
    private final String subTitle;

    public ItemStack asItemStack() {
        ItemBuilder clone = this.displayItem.clone();

        if (!displayName.isEmpty()) {
            clone.name(MessageUtils.translateColorCodes(displayName));
        }

        return this.asItemStack(clone.clone());
    }

    public final ItemStack asItemStack(int amount) {
        ItemBuilder clone = this.displayItem.clone();
        return this.asItemStack(clone.clone().amount(amount));
    }

    public final ItemStack asItemStack(ItemBuilder itemBuilder) {
        return itemBuilder.clone().make();
    }

    public boolean hasTitle() {
        return !title.isEmpty() && !subTitle.isEmpty();
    }

    public boolean hasChat() {
        return !chatMessage.isEmpty();
    }
}

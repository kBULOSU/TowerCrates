package br.com.yiatzz.crates.crate;

import br.com.idea.api.shared.misc.utils.RandomList;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.ArmorStand;

import java.util.LinkedList;

@Getter
@EqualsAndHashCode(of = {"name"})
public class Crate {

    private final String name;
    private final String displayName;

    private final String headKey;

    @Setter
    private ArmorStand head;

    private final LinkedList<CrateItem> items;

    public Crate(String name, String displayName, String headKey, LinkedList<CrateItem> items) {
        this.name = name;
        this.displayName = displayName;
        this.headKey = headKey;
        this.items = items;
    }

    public CrateItem getRandomItem() {
        RandomList<CrateItem> random = new RandomList<>();

        for (CrateItem crateItem : this.items) {
            random.add(crateItem, crateItem.getWeight());
        }

        return random.raffle();
    }

}

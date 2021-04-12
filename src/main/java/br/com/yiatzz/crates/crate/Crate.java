package br.com.yiatzz.crates.crate;

import br.com.idea.api.shared.misc.utils.RandomList;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
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
    private final LinkedList<CrateItem> items;
    @Setter
    private ArmorStand head;
    @Setter
    private Hologram hologram;
    @Setter
    private LinkedList<String> hologramLines;

    public Crate(String name, String displayName, String headKey, LinkedList<CrateItem> items, LinkedList<String> hologramLines) {
        this.name = name;
        this.displayName = displayName;
        this.headKey = headKey;
        this.items = items;
        this.hologramLines = hologramLines;
    }

    public CrateItem getRandomItem() {
        RandomList<CrateItem> random = new RandomList<>();

        for (CrateItem crateItem : this.items) {
            random.add(crateItem, crateItem.getWeight());
        }

        return random.raffle();
    }

}

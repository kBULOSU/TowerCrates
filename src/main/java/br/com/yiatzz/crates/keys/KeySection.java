package br.com.yiatzz.crates.keys;

import lombok.Getter;

@Getter
public class KeySection {

    private final String crateName;

    private final String displayName;
    private final String[] lore;

    public KeySection(String crateName, String displayName, String[] lore) {
        this.crateName = crateName;
        this.displayName = displayName;
        this.lore = lore;
    }
}

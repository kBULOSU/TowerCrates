package br.com.yiatzz.crates.keys.cache.local;

import br.com.idea.api.shared.cache.LocalCache;
import br.com.yiatzz.crates.keys.KeySection;
import com.google.common.collect.Maps;

import java.util.Map;

public class KeysSectionLocalCache implements LocalCache {

    private final Map<String, KeySection> cache = Maps.newHashMap();

    public KeySection get(String crateName) {
        return cache.get(crateName);
    }

    public void put(KeySection section) {
        cache.put(section.getCrateName(), section);
    }

}

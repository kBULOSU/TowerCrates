package br.com.yiatzz.crates.cache.local;

import br.com.idea.api.shared.cache.LocalCache;
import br.com.yiatzz.crates.CratesProvider;
import br.com.yiatzz.crates.crate.Crate;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.collect.Maps;
import org.bukkit.Location;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CratesLocalCache implements LocalCache {

    public final Map<String, Crate> CACHE = Maps.newHashMap();

    private final LoadingCache<String, Location> LOCATIONS = Caffeine.newBuilder()
            .expireAfterWrite(5L, TimeUnit.SECONDS)
            .build(name -> CratesProvider.Repositories.LOCATIONS.provide().fetch(name));

    public Crate get(String name) {
        return CACHE.get(name);
    }

    public Location getLocation(String name) {
        return LOCATIONS.get(name);
    }

    public void add(String name, Crate box) {
        CACHE.put(name, box);
    }

    public void invalidate(String name) {
        LOCATIONS.invalidate(name);
    }

    public void remove(String name) {
        CACHE.remove(name);
    }

}

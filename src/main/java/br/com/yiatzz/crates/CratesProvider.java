package br.com.yiatzz.crates;

import br.com.idea.api.shared.ApiProvider;
import br.com.idea.api.shared.providers.LocalCacheProvider;
import br.com.idea.api.shared.providers.MysqlRepositoryProvider;
import br.com.yiatzz.crates.cache.local.CratesLocalCache;
import br.com.yiatzz.crates.keys.cache.local.KeysSectionLocalCache;
import br.com.yiatzz.crates.storage.CratesLocationsRepository;

public class CratesProvider {

    public static void prepare() {
        Repositories.LOCATIONS.prepare();

        Cache.Local.CRATES.prepare();
        Cache.Local.KEYS.prepare();
    }

    public static class Repositories {

        public static final MysqlRepositoryProvider<CratesLocationsRepository> LOCATIONS = new MysqlRepositoryProvider<>(
                () -> ApiProvider.Database.MySQL.MYSQL_MAIN,
                CratesLocationsRepository.class
        );

    }

    public static class Cache {

        public static class Local {

            public static final LocalCacheProvider<CratesLocalCache> CRATES = new LocalCacheProvider<>(
                    new CratesLocalCache()
            );

            public static final LocalCacheProvider<KeysSectionLocalCache> KEYS = new LocalCacheProvider<>(
                    new KeysSectionLocalCache()
            );

        }

    }

}

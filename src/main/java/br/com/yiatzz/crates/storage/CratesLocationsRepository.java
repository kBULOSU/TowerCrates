package br.com.yiatzz.crates.storage;

import br.com.idea.api.shared.providers.MysqlDatabaseProvider;
import br.com.idea.api.shared.storage.repositories.MysqlRepository;
import br.com.yiatzz.crates.storage.specs.CreateLocationsTableSpec;
import br.com.yiatzz.crates.storage.specs.InsertOrUpdateLocationSpec;
import br.com.yiatzz.crates.storage.specs.SelectLocationSpec;
import org.bukkit.Location;

public class CratesLocationsRepository extends MysqlRepository {

    public CratesLocationsRepository(MysqlDatabaseProvider databaseProvider) {
        super(databaseProvider);

        query(new CreateLocationsTableSpec());
    }

    public Location fetch(String name) {
        return query(new SelectLocationSpec(name));
    }

    public void insertOrUpdate(String name, Location location) {
        query(new InsertOrUpdateLocationSpec(location, name));
    }
}

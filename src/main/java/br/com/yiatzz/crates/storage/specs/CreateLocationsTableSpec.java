package br.com.yiatzz.crates.storage.specs;

import br.com.idea.api.shared.storage.repositories.specs.ExecuteSqlSpec;
import br.com.idea.api.shared.storage.repositories.specs.PreparedStatementCallback;
import br.com.idea.api.shared.storage.repositories.specs.PreparedStatementCreator;

public class CreateLocationsTableSpec extends ExecuteSqlSpec<Void> {

    @Override
    public PreparedStatementCallback<Void> getPreparedStatementCallback() {
        return null;
    }

    @Override
    public PreparedStatementCreator getPreparedStatementCreator() {
        return connection -> {
            String query = String.format(
                    "CREATE TABLE IF NOT EXISTS `%s` (`name` VARCHAR(255) NOT NULL PRIMARY KEY, `worldName` VARCHAR(255) NOT NULL, `x` DOUBLE NOT NULL, " +
                            "`y` DOUBLE NOT NULL, `z` DOUBLE NOT NULL);",
                    "crates_locations"
            );

            return connection.prepareStatement(query);
        };
    }
}

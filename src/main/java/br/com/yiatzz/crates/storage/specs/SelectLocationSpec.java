package br.com.yiatzz.crates.storage.specs;

import br.com.idea.api.shared.storage.repositories.specs.PreparedStatementCreator;
import br.com.idea.api.shared.storage.repositories.specs.ResultSetExtractor;
import br.com.idea.api.shared.storage.repositories.specs.SelectSqlSpec;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.PreparedStatement;

@RequiredArgsConstructor
public class SelectLocationSpec extends SelectSqlSpec<Location> {

    private final String name;

    @Override
    public ResultSetExtractor<Location> getResultSetExtractor() {
        return resultSet -> {
            if (resultSet.next()) {
                return new Location(
                        Bukkit.getWorld(resultSet.getString("worldName")),
                        resultSet.getDouble("x"),
                        resultSet.getDouble("y"),
                        resultSet.getDouble("z")
                );
            }

            return null;
        };
    }

    @Override
    public PreparedStatementCreator getPreparedStatementCreator() {
        return connection -> {
            String query = String.format(
                    "SELECT * FROM `%s` WHERE `name` = ? LIMIT 1;",
                    "crates_locations"
            );

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);

            return preparedStatement;
        };
    }
}

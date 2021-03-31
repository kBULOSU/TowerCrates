package br.com.yiatzz.crates.storage.specs;

import br.com.idea.api.shared.storage.repositories.specs.InsertSqlSpec;
import br.com.idea.api.shared.storage.repositories.specs.PreparedStatementCreator;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
public class InsertOrUpdateLocationSpec extends InsertSqlSpec<Void> {

    private final Location location;
    private final String name;

    @Override
    public Void parser(int i, ResultSet resultSet) throws SQLException {
        return null;
    }

    @Override
    public PreparedStatementCreator getPreparedStatementCreator() {
        return connection -> {
            String query = String.format(
                    "INSERT INTO `%s` (`name`, `worldName`, `x`, `y`, `z`) VALUES (?,?,?,?,?) ON DUPLICATE KEY UPDATE " +
                            "`worldName`=VALUES(worldName), `x`=VALUES(x), `y`=VALUES(y), `z`=VALUES(z);",
                    "crates_locations"
            );

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, location.getWorld().getName());
            preparedStatement.setDouble(3, location.getX());
            preparedStatement.setDouble(4, location.getY());
            preparedStatement.setDouble(5, location.getZ());

            return preparedStatement;
        };
    }
}

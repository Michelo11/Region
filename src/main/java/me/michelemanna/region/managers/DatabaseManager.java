package me.michelemanna.region.managers;

import me.michelemanna.region.data.Region;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DatabaseManager {
    private final Connection connection;

    public DatabaseManager(String url) throws SQLException {
        connection = DriverManager.getConnection(url);

        Statement statement = connection.createStatement();
        statement.execute(
                "CREATE TABLE IF NOT EXISTS regions (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "player VARCHAR(36) NOT NULL," +
                        "name TEXT NOT NULL," +
                        "world TEXT NOT NULL," +
                        "x DOUBLE NOT NULL," +
                        "y DOUBLE NOT NULL," +
                        "z DOUBLE NOT NULL," +
                        "x2 DOUBLE NOT NULL," +
                        "y2 DOUBLE NOT NULL," +
                        "z2 DOUBLE NOT NULL," +
                        ")"
        );
        statement.execute(
                "CREATE TABLE IF NOT EXISTS whitelist (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "region_id INTEGER NOT NULL," +
                        "FOREIGN KEY(region_id) REFERENCES regions(id)" +
                        ")"
        );
        statement.close();
    }

    public Map<String, Region> getRegions() throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM regions;");

        ResultSet result = statement.executeQuery();

        Map<String, Region> regions = new HashMap<>();

        while (result.next()) {
            String name = result.getString("name");
            String world = result.getString("world");
            double x = result.getDouble("x");
            double y = result.getDouble("y");
            double z = result.getDouble("z");
            double x2 = result.getDouble("x2");
            double y2 = result.getDouble("y2");
            double z2 = result.getDouble("z2");

            Location start = new Location(Bukkit.getWorld(world), x, y, z);
            Location end = new Location(Bukkit.getWorld(world), x2, y2, z2);

            regions.put(name, new Region(name, start, end, UUID.fromString(result.getString("player"))));
        }

        result.close();
        statement.close();

        return regions;
    }
}

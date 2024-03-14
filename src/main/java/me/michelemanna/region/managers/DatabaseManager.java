package me.michelemanna.region.managers;

import me.michelemanna.region.RegionPlugin;
import me.michelemanna.region.data.Region;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.ArrayList;

public class DatabaseManager {
    private final Connection connection;

    public DatabaseManager(String url) throws SQLException {
        connection = DriverManager.getConnection(url);

        Statement statement = connection.createStatement();

        statement.execute(
                "CREATE TABLE IF NOT EXISTS regions (" +
                        "id INT PRIMARY KEY AUTO_INCREMENT," +
                        "player VARCHAR(36) NOT NULL," +
                        "name TEXT NOT NULL," +
                        "world TEXT NOT NULL," +
                        "x DOUBLE NOT NULL," +
                        "y DOUBLE NOT NULL," +
                        "z DOUBLE NOT NULL," +
                        "x2 DOUBLE NOT NULL," +
                        "y2 DOUBLE NOT NULL," +
                        "z2 DOUBLE NOT NULL" +
                        ")"
        );
        statement.execute(
                "CREATE TABLE IF NOT EXISTS whitelist (" +
                        "id INT PRIMARY KEY AUTO_INCREMENT," +
                        "region_id INTEGER NOT NULL," +
                        "player VARCHAR(36) NOT NULL," +
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
            int id = result.getInt("id");
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

            regions.put(name, new Region(id, name, start, end, UUID.fromString(result.getString("player"))));
        }

        result.close();
        statement.close();

        return regions;
    }

    public void createRegion(Region region) {
        Bukkit.getScheduler().runTaskAsynchronously(RegionPlugin.getInstance(), () -> {
            try {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO regions (player, name, world, x, y, z, x2, y2, z2) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");

                statement.setString(1, region.owner().toString());
                statement.setString(2, region.name());
                statement.setString(3, region.start().getWorld().getName());
                statement.setDouble(4, region.start().getX());
                statement.setDouble(5, region.start().getY());
                statement.setDouble(6, region.start().getZ());
                statement.setDouble(7, region.end().getX());
                statement.setDouble(8, region.end().getY());
                statement.setDouble(9, region.end().getZ());

                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void updateName(Region region, String name) {
        Bukkit.getScheduler().runTaskAsynchronously(RegionPlugin.getInstance(), () -> {
            try {
                PreparedStatement statement = connection.prepareStatement("UPDATE regions SET name = ? WHERE id = ?;");

                statement.setString(1, name);
                statement.setInt(2, region.id());

                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void updateLocation(Region region) {
        Bukkit.getScheduler().runTaskAsynchronously(RegionPlugin.getInstance(), () -> {
            try {
                PreparedStatement statement = connection.prepareStatement("UPDATE regions SET world = ?, x = ?, y = ?, z = ?, x2 = ?, y2 = ?, z2 = ? WHERE id = ?;");

                statement.setString(1, region.start().getWorld().getName());
                statement.setDouble(2, region.start().getX());
                statement.setDouble(3, region.start().getY());
                statement.setDouble(4, region.start().getZ());
                statement.setDouble(5, region.end().getX());
                statement.setDouble(6, region.end().getY());
                statement.setDouble(7, region.end().getZ());
                statement.setInt(8, region.id());

                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public List<UUID> getWhitelist(Region region) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT player FROM whitelist WHERE region_id = ?;");

            statement.setInt(1, region.id());

            ResultSet result = statement.executeQuery();

            List<UUID> whitelist = new ArrayList<>();

            while (result.next()) {
                whitelist.add(UUID.fromString(result.getString("player")));
            }

            result.close();
            statement.close();

            return whitelist;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void addWhitelist(Region region, Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(RegionPlugin.getInstance(), () -> {
            try {
                PreparedStatement statement = connection.prepareStatement("SELECT * FROM whitelist WHERE region_id = ? AND player = ?;");

                statement.setInt(1, region.id());
                statement.setString(2, player.getUniqueId().toString());

                ResultSet result = statement.executeQuery();

                if (result.next()) {
                    result.close();
                    statement.close();
                    return;
                }

                statement = connection.prepareStatement("INSERT INTO whitelist (region_id, player) VALUES (?, ?);");

                statement.setInt(1, region.id());
                statement.setString(2, player.getUniqueId().toString());

                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void removeWhiteList(Region region, Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(RegionPlugin.getInstance(), () -> {
            try {
                PreparedStatement statement = connection.prepareStatement("DELETE FROM whitelist WHERE region_id = ? AND player = ?;");

                statement.setInt(1, region.id());
                statement.setString(2, player.getUniqueId().toString());

                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}

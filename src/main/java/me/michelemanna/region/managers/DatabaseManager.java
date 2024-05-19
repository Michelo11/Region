package me.michelemanna.region.managers;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.michelemanna.region.RegionPlugin;
import me.michelemanna.region.data.Flag;
import me.michelemanna.region.data.FlagState;
import me.michelemanna.region.data.Region;
import me.michelemanna.region.managers.providers.DatabaseProvider;
import me.michelemanna.region.managers.providers.MySQLProvider;
import me.michelemanna.region.managers.providers.SQLiteProvider;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class DatabaseManager {
    private final DatabaseProvider provider;
    private final Gson gson = new Gson();

    public DatabaseManager(RegionPlugin plugin) throws SQLException, ClassNotFoundException, IOException {
        ConfigurationSection cs = RegionPlugin.getInstance()
                .getConfig()
                .getConfigurationSection("mysql");
        Objects.requireNonNull(cs, "Unable to find the following key: mysql");

        if (cs.getString("type", "mysql").equalsIgnoreCase("mysql")) {
            provider = new MySQLProvider();
        } else
            provider = new SQLiteProvider();

        provider.connect();
    }

    public CompletableFuture<Map<String, Region>> getRegions() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Connection connection = provider.getConnection();
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
                    Map<Flag, FlagState> flags = gson.fromJson(result.getString("flags"), new HashMap<Flag, FlagState>().getClass());

                    regions.put(name, new Region(id, name, start, end, UUID.fromString(result.getString("player")), new ArrayList<>(), flags));
                }

                result.close();
                statement.close();
                provider.closeConnection(connection);

                return regions;

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return new HashMap<>();
        }, (runnable) -> Bukkit.getScheduler().runTaskAsynchronously(RegionPlugin.getInstance(), runnable));
    }

    public void createRegion(Region region) {
        Bukkit.getScheduler().runTaskAsynchronously(RegionPlugin.getInstance(), () -> {
            try {
                Connection connection = provider.getConnection();

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
                provider.closeConnection(connection);

                RegionPlugin.getInstance().getRegionManager().getRegions().put(region.name(), region);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void deleteRegion(Region region) {
        Bukkit.getScheduler().runTaskAsynchronously(RegionPlugin.getInstance(), () -> {
            try {
                Connection connection = provider.getConnection();

                PreparedStatement statement = connection.prepareStatement("DELETE FROM regions WHERE id = ?;");

                statement.setInt(1, region.id());

                statement.executeUpdate();
                statement.close();
                provider.closeConnection(connection);

                RegionPlugin.getInstance().getRegionManager().getRegions().remove(region.name());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void updateName(Region region, String name) {
        Bukkit.getScheduler().runTaskAsynchronously(RegionPlugin.getInstance(), () -> {
            try {
                Connection connection = provider.getConnection();

                PreparedStatement statement = connection.prepareStatement("UPDATE regions SET name = ? WHERE id = ?;");

                statement.setString(1, name);
                statement.setInt(2, region.id());

                statement.executeUpdate();
                statement.close();
                provider.closeConnection(connection);

                RegionPlugin.getInstance().getRegionManager().getRegions().remove(region.name());
                RegionPlugin.getInstance().getRegionManager().getRegions().put(name, new Region(
                        region.id(),
                        name,
                        region.start(),
                        region.end(),
                        region.owner(),
                        region.whitelist(),
                        region.flags()
                ));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void updateLocation(Region region) {
        Bukkit.getScheduler().runTaskAsynchronously(RegionPlugin.getInstance(), () -> {
            try {
                Connection connection = provider.getConnection();

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
                provider.closeConnection(connection);

                RegionPlugin.getInstance().getRegionManager().getRegions().put(region.name(), region);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void updateFlag(Region region, Flag flag, FlagState state) {
        Bukkit.getScheduler().runTaskAsynchronously(RegionPlugin.getInstance(), () -> {
            try {
                Connection connection = provider.getConnection();

                PreparedStatement statement = connection.prepareStatement("UPDATE regions SET flags = ? WHERE id = ?;");

                region.flags().put(flag, state);

                statement.setString(1, gson.toJson(region.flags()));
                statement.setInt(2, region.id());

                statement.executeUpdate();
                statement.close();
                provider.closeConnection(connection);

                RegionPlugin.getInstance().getRegionManager().getRegions().put(region.name(), region);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public CompletableFuture<List<UUID>> getWhitelist(Region region) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Connection connection = provider.getConnection();

                PreparedStatement statement = connection.prepareStatement("SELECT player FROM whitelist WHERE region_id = ?;");

                statement.setInt(1, region.id());

                ResultSet result = statement.executeQuery();

                List<UUID> whitelist = new ArrayList<>();

                while (result.next()) {
                    whitelist.add(UUID.fromString(result.getString("player")));
                }

                result.close();
                statement.close();
                provider.closeConnection(connection);

                return whitelist;
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        }, (runnable) -> Bukkit.getScheduler().runTaskAsynchronously(RegionPlugin.getInstance(), runnable));
    }

    public void addWhitelist(Region region, Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(RegionPlugin.getInstance(), () -> {
            try {
                Connection connection = provider.getConnection();

                PreparedStatement statement = connection.prepareStatement("SELECT * FROM whitelist WHERE region_id = ? AND player = ?;");

                statement.setInt(1, region.id());
                statement.setString(2, player.getUniqueId().toString());

                ResultSet result = statement.executeQuery();

                if (result.next()) {
                    result.close();
                    statement.close();
                    connection.close();
                    return;
                }

                statement = connection.prepareStatement("INSERT INTO whitelist (region_id, player) VALUES (?, ?);");

                statement.setInt(1, region.id());
                statement.setString(2, player.getUniqueId().toString());

                statement.executeUpdate();
                statement.close();
                provider.closeConnection(connection);

                region.whitelist().add(player.getUniqueId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void removeWhiteList(Region region, Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(RegionPlugin.getInstance(), () -> {
            try {
                Connection connection = provider.getConnection();

                PreparedStatement statement = connection.prepareStatement("DELETE FROM whitelist WHERE region_id = ? AND player = ?;");

                statement.setInt(1, region.id());
                statement.setString(2, player.getUniqueId().toString());

                statement.executeUpdate();
                statement.close();
                provider.closeConnection(connection);

                region.whitelist().remove(player.getUniqueId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void close() throws SQLException {
        provider.disconnect();
    }
}

package me.michelemanna.region;

import me.michelemanna.region.data.Region;
import me.michelemanna.region.managers.DatabaseManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public final class RegionPlugin extends JavaPlugin {
    private final Map<String, Region> regions = new HashMap<>();
    private DatabaseManager database;
    private static RegionPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();

        try {
            this.database = new DatabaseManager(this.getConfig().getString("database"));
            this.regions.putAll(this.database.getRegions());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static RegionPlugin getInstance() {
        return instance;
    }

    public DatabaseManager getDatabase() {
        return database;
    }
}

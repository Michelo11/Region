package me.michelemanna.region;

import me.michelemanna.region.commands.RegionCommand;
import me.michelemanna.region.listeners.RegionListener;
import me.michelemanna.region.listeners.WandListener;
import me.michelemanna.region.managers.DatabaseManager;
import me.michelemanna.region.managers.RegionManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;

import java.sql.SQLException;

public final class RegionPlugin extends JavaPlugin {
    private DatabaseManager database;
    private RegionManager regionManager;
    private WandListener wandListener;
    private static RegionPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        getCommand("region").setExecutor(new RegionCommand(this));
        Bukkit.getPluginManager().registerEvents(wandListener = new WandListener(), this);
        Bukkit.getPluginManager().registerEvents(new RegionListener(), this);

        try {
            this.database = new DatabaseManager(this);

            this.regionManager = new RegionManager(this);
            this.regionManager.load();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        this.database.close();
    }

    public String getMessage(String path) {
        return ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("messages." + path, "&cMessage not found"));
    }

    public static RegionPlugin getInstance() {
        return instance;
    }

    public DatabaseManager getDatabase() {
        return database;
    }

    public WandListener getWandListener() {
        return wandListener;
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }
}

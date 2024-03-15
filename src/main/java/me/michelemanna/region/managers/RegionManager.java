package me.michelemanna.region.managers;

import me.michelemanna.region.RegionPlugin;
import me.michelemanna.region.data.Region;

import java.util.HashMap;
import java.util.Map;

public class RegionManager {
    private final Map<String, Region> regions = new HashMap<>();
    private final RegionPlugin plugin;

    public RegionManager(RegionPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        plugin.getDatabase().getRegions().thenAccept(regions -> {
            this.regions.putAll(regions);

            for (Region region : regions.values()) {
                plugin.getDatabase().getWhitelist(region).thenAccept(whitelist -> {
                    region.whitelist().addAll(whitelist);
                });
            }
        });
    }

    public Region getRegion(String name) {
        return regions.get(name);
    }

    public Map<String, Region> getRegions() {
        return regions;
    }
}
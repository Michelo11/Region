package me.michelemanna.region.data;

import org.bukkit.Location;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record Region (int id, String name, Location start, Location end, UUID owner, List<UUID> whitelist, Map<Flag, FlagState> flags) {
    public boolean contains(Location location) {
        return location.getWorld().equals(start.getWorld()) &&
                location.getX() >= Math.min(start.getX(), end.getX()) &&
                location.getX() <= Math.max(start.getX(), end.getX()) &&
                location.getY() >= Math.min(start.getY(), end.getY()) &&
                location.getY() <= Math.max(start.getY(), end.getY()) &&
                location.getZ() >= Math.min(start.getZ(), end.getZ()) &&
                location.getZ() <= Math.max(start.getZ(), end.getZ());
    }

    public FlagState getFlagState(Flag flag) {
        return flags.getOrDefault(flag, FlagState.WHITELIST);
    }
}
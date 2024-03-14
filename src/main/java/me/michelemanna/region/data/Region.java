package me.michelemanna.region.data;

import org.bukkit.Location;

import java.util.UUID;

public record Region (int id, String name, Location start, Location end, UUID owner) {}
package me.michelemanna.region.listeners;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class WandListener implements Listener {
    private final Map<Player, Location> startLocations = new HashMap<>();
    private  final Map<Player, Location> endLocations = new HashMap<>();

    @EventHandler
    public void onWandUse(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) {
            return;
        }

        ItemStack item = event.getItem();

        if (item == null) {
            return;
        }

        if (item.getType() == Material.WOODEN_AXE) {
            NBTItem nbtItem = new NBTItem(item);

            if (!nbtItem.hasTag("region_wand")) {
                return;
            }

            event.setCancelled(true);
            if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                startLocations.put(event.getPlayer(), event.getClickedBlock().getLocation());

                event.getPlayer().sendMessage("§aStart location set!");
            } else if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                endLocations.put(event.getPlayer(), event.getClickedBlock().getLocation());

                event.getPlayer().sendMessage("§aEnd location set!");
            }
        }
    }

    public Map<Player, Location> getStartLocations() {
        return startLocations;
    }

    public Map<Player, Location> getEndLocations() {
        return endLocations;
    }
}
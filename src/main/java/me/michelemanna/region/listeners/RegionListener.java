package me.michelemanna.region.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import me.michelemanna.region.RegionPlugin;
import me.michelemanna.region.data.Region;

public class RegionListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getPlayer().hasPermission("region.bypass")) {
            return;
        }

        Block clickedBlock = event.getClickedBlock();
        
        if (clickedBlock == null) {
            return;
        }

        Region region = RegionPlugin.getInstance()
            .getRegionManager()
            .getRegions()
            .values()
            .stream()
            .filter(r -> r.contains(clickedBlock.getLocation()))
            .findFirst()
            .orElse(null);
        
        if (region == null) {
            return;
        }

        region.whitelist()
            .stream()
            .filter(uuid -> uuid.equals(event.getPlayer().getUniqueId()))
            .findFirst()
            .ifPresentOrElse(uuid -> {}, () -> {
                event.setCancelled(true);
                event.getPlayer().sendMessage("§cYou do not have permission to interact with this block!");
            });

    }
}
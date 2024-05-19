package me.michelemanna.region.listeners;

import me.michelemanna.region.data.Flag;
import me.michelemanna.region.data.FlagState;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.michelemanna.region.RegionPlugin;
import me.michelemanna.region.data.Region;

import java.util.Optional;
import java.util.UUID;

public class RegionListener implements Listener {
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();

        if (block == null) {
            return;
        }

        if (!canPerform(block.getLocation(), player, Flag.INTERACT)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        if (!canPerform(block.getLocation(), player, Flag.BLOCK_BREAK)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        if (!canPerform(block.getLocation(), player, Flag.BLOCK_PLACE)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getDamager();

        if (!canPerform(player.getLocation(), player, Flag.ENTITY_DAMAGE)) {
            event.setCancelled(true);
        }
    }

    private boolean canPerform(Location location, Player player, Flag flag) {
        if (player.hasPermission("region.bypass")) {
            return true;
        }

        Region region = RegionPlugin.getInstance()
                .getRegionManager()
                .getRegions()
                .values()
                .stream()
                .filter(r -> r.contains(location))
                .findFirst()
                .orElse(null);

        if (region == null || region.getFlagState(flag).equals(FlagState.EVERYONE)) {
            return true;
        }

        if (region.getFlagState(flag).equals(FlagState.NONE)) {
            player.sendMessage(RegionPlugin.getInstance().getMessage("listeners.no-permission"));
            return false;
        }

        Optional<UUID> first = region.whitelist()
                .stream()
                .filter(uuid -> uuid.equals(player.getUniqueId()))
                .findFirst();

        if (first.isEmpty()) {
            player.sendMessage(RegionPlugin.getInstance().getMessage("listeners.no-permission"));
        }

        return first.isPresent();
    }
}
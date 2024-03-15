package me.michelemanna.region.gui.items;

import me.michelemanna.region.RegionPlugin;
import me.michelemanna.region.data.Region;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class RedefineLocationItem extends AbstractItem {
    private final RegionPlugin plugin = RegionPlugin.getInstance();
    private final Region region;

    public RedefineLocationItem(Region region) {
        this.region = region;
    }

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.COMPASS)
                .setDisplayName("Redefine Location");
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        if (plugin.getWandListener().getStartLocations().get(player) == null || plugin.getWandListener().getEndLocations().get(player) == null) {
            player.sendMessage("§cYou need to select the region with the wand first!");
            return;
        }

        if (region.start().equals(plugin.getWandListener().getStartLocations().get(player)) && region.end().equals(plugin.getWandListener().getEndLocations().get(player))) {
            player.sendMessage("§cYou need to change the region's location!");
            return;
        }

        plugin.getDatabase().updateLocation(new Region(
                region.id(),
                region.name(),
                plugin.getWandListener().getStartLocations().get(player),
                plugin.getWandListener().getEndLocations().get(player),
                player.getUniqueId(),
                region.whitelist()
        ));

        player.sendMessage("§aRegion updated!");
    }
}

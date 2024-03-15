package me.michelemanna.region.gui;

import me.michelemanna.region.RegionPlugin;
import me.michelemanna.region.data.Region;
import me.michelemanna.region.gui.items.RegionsItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.window.Window;

import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

public class RegionsMenu {
    public static void openRegions(Player player) {
        Map<String, Region> regions = RegionPlugin.getInstance().getRegionManager().getRegions();

        if (regions.isEmpty()) {
            player.sendMessage("§cYou don't have any regions set!");
            return;
        }

        PagedGui<?> gui = PagedGui.items()
                .setStructure(
                        "# # # # # # # # #",
                        "# . . . . . . . #",
                        "# . . . . . . . #",
                        "# # # # # # # # #"
                )
                .setContent(regions.values().stream()
                        .map(region -> new RegionsItem(region))
                        .collect(Collectors.toList()))
                .addIngredient('#', new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(""))
                .addIngredient('.', Markers.CONTENT_LIST_SLOT_HORIZONTAL).build();


        Window window = Window.single()
                .setViewer(player)
                .setTitle("Regions")
                .setGui(gui)
                .build();

        window.open();
    }
}
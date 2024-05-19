package me.michelemanna.region.gui;

import me.michelemanna.region.data.Flag;
import me.michelemanna.region.data.Region;
import me.michelemanna.region.gui.items.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.window.Window;

public class FlagsMenu {
    public static void openFlags(Player player, Region region) {
        PagedGui<?> gui = PagedGui.items()
                .setStructure(
                        "# # # # # # # # #",
                        "# b . p . i . e #",
                        "# . . . . . . . #",
                        "# # # # # # # # #"
                )
                .addIngredient('#', new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(""))
                .addIngredient('.', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('b' , new FlagItem(region, Flag.BLOCK_BREAK))
                .addIngredient('p', new FlagItem(region, Flag.BLOCK_PLACE))
                .addIngredient('i', new FlagItem(region, Flag.INTERACT))
                .addIngredient('e', new FlagItem(region, Flag.ENTITY_DAMAGE))
                .build();

        Window window = Window.single()
                .setViewer(player)
                .setTitle("Flags")
                .setGui(gui)
                .build();

        window.open();
    }
}
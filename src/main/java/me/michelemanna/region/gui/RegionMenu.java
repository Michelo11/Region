package me.michelemanna.region.gui;

import me.michelemanna.region.data.Region;
import me.michelemanna.region.gui.items.RedefineLocationItem;
import me.michelemanna.region.gui.items.RenameItem;
import me.michelemanna.region.gui.items.WhiteListAddItem;
import me.michelemanna.region.gui.items.WhiteListRemoveItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.window.Window;

public class RegionMenu {
    public static void openRegion(Player player, Region region) {
        PagedGui<?> gui = PagedGui.items()
                .setStructure(
                        "# # # # # # # # #",
                        "# r . a . w . l #",
                        "# . . . . . . . #",
                        "# # # # # # # # #"
                )
                .addIngredient('#', new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(""))
                .addIngredient('.', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('r' , new RenameItem(region))
                .addIngredient('a', new WhiteListAddItem(region))
                .addIngredient('w', new WhiteListRemoveItem(region))
                .addIngredient('l', new RedefineLocationItem(region))
                .build();

        Window window = Window.single()
                .setViewer(player)
                .setTitle("Regions")
                .setGui(gui)
                .build();

        window.open();
    }
}

package me.michelemanna.region.gui.items;

import me.michelemanna.region.RegionPlugin;
import me.michelemanna.region.data.Flag;
import me.michelemanna.region.data.FlagState;
import me.michelemanna.region.data.Region;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.util.Arrays;

public class FlagItem extends AbstractItem {
    private final Region region;
    private final Flag flag;

    public FlagItem(Region region, Flag flag) {
        this.region = region;
        this.flag = flag;
    }

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.PAPER)
                .setDisplayName(flag.name())
                .setLegacyLore(Arrays.asList(
                        RegionPlugin.getInstance().getConfig().getString("messages.gui.current-flag")
                                .replace("%state%", region.getFlagState(flag).name())
                ));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        FlagState current = region.getFlagState(flag);

        FlagState next = current.next();

        RegionPlugin.getInstance().getDatabase().updateFlag(region, flag, next);

        region.flags().put(flag, next);

        this.notifyWindows();
    }
}

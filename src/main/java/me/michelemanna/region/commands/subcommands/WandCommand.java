package me.michelemanna.region.commands.subcommands;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.michelemanna.region.RegionPlugin;
import me.michelemanna.region.commands.SubCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.xenondevs.invui.item.builder.ItemBuilder;

public class WandCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        if (args.length != 3) {
            player.sendMessage(RegionPlugin.getInstance().getMessage("commands.wand-usage"));
            return;
        }

        ItemBuilder wand = new ItemBuilder(Material.WOODEN_AXE);
        wand.setDisplayName("Region Wand");
        ItemStack item = wand.get();

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setBoolean("region_wand", true);
        player.getInventory().addItem(nbtItem.getItem());
        player.sendMessage(RegionPlugin.getInstance().getMessage("commands.wand-success"));
    }
}
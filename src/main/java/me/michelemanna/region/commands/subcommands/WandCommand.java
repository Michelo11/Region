package me.michelemanna.region.commands.subcommands;

import me.michelemanna.region.commands.SubCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.item.builder.ItemBuilder;

public class WandCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        ItemBuilder wand = new ItemBuilder(Material.WOODEN_AXE);
        wand.setDisplayName("Region Wand");
        player.getInventory().addItem(wand.get());
        player.sendMessage("§aYou have been given the region wand!");
    }
}
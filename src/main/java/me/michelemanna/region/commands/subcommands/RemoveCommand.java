package me.michelemanna.region.commands.subcommands;

import me.michelemanna.region.RegionPlugin;
import me.michelemanna.region.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RemoveCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission("region.remove")) {
            player.sendMessage("§cYou do not have permission to use this command!");
            return;
        }

        if (args.length != 3) {
            player.sendMessage("§cYou need to specify a player to remove from the region!");
            return;
        }

        if (RegionPlugin.getInstance()
                .getRegionManager()
                .getRegion(args[1]) == null) {
            player.sendMessage("§cThe region does not exist!");
            return;
        }

        Player target = Bukkit.getPlayer(args[2]);

        if (target == null) {
            player.sendMessage("§cThe player is not online!");
            return;
        }

        RegionPlugin.getInstance()
                .getDatabase()
                .removeWhiteList(RegionPlugin.getInstance()
                        .getRegionManager()
                        .getRegion(args[1]), target);

        player.sendMessage("§aPlayer removed from the region!");
    }
}

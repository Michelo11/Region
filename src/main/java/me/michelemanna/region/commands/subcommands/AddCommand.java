package me.michelemanna.region.commands.subcommands;

import me.michelemanna.region.RegionPlugin;
import me.michelemanna.region.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class AddCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission("region.add")) {
            player.sendMessage(RegionPlugin.getInstance().getMessage("commands.no-permission"));
            return;
        }

        if (args.length != 3) {
            player.sendMessage(RegionPlugin.getInstance().getMessage("commands.add-usage"));
            return;
        }

        if (RegionPlugin.getInstance().getRegionManager().getRegion(args[1]) == null) {
            player.sendMessage(RegionPlugin.getInstance().getMessage("commands.region-not-found"));
            return;
        }

        Player target = Bukkit.getPlayer(args[2]);

        if (target == null) {
            player.sendMessage(RegionPlugin.getInstance().getMessage("commands.player-not-found"));
            return;
        }

        RegionPlugin.getInstance()
                .getDatabase()
                .addWhitelist(RegionPlugin.getInstance()
                        .getRegionManager().getRegion(args[1]), target);

        player.sendMessage(RegionPlugin.getInstance().getMessage("commands.add-success"));
    }
}

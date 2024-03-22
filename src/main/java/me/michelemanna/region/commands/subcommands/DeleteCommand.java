package me.michelemanna.region.commands.subcommands;

import me.michelemanna.region.RegionPlugin;
import me.michelemanna.region.commands.SubCommand;
import me.michelemanna.region.data.Region;
import org.bukkit.entity.Player;

public class DeleteCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission("region.delete")) {
            player.sendMessage(RegionPlugin.getInstance().getMessage("commands.no-permission"));
            return;
        }

        if (args.length != 2) {
            player.sendMessage(RegionPlugin.getInstance().getMessage("commands.delete-usage"));
            return;
        }

        Region region = RegionPlugin.getInstance().getRegionManager().getRegion(args[1]);

        if (region == null) {
            player.sendMessage(RegionPlugin.getInstance().getMessage("commands.region-not-found"));
            return;
        }

        RegionPlugin.getInstance().getDatabase().deleteRegion(region);

        player.sendMessage(RegionPlugin.getInstance().getMessage("commands.delete-success"));
    }
}

package me.michelemanna.region.commands.subcommands;

import me.michelemanna.region.RegionPlugin;
import me.michelemanna.region.commands.SubCommand;
import me.michelemanna.region.data.Region;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CreateCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission("region.create")) {
            player.sendMessage(RegionPlugin.getInstance().getMessage("commands.no-permission"));
            return;
        }

        if (args.length != 2) {
            player.sendMessage(RegionPlugin.getInstance().getMessage("commands.create-usage"));
            return;
        }

        if (RegionPlugin.getInstance()
                .getWandListener()
                .getStartLocations()
                .get(player) == null || RegionPlugin.getInstance()
                .getWandListener()
                .getEndLocations()
                .get(player) == null) {
            player.sendMessage(RegionPlugin.getInstance().getMessage("commands.create-no-wand"));
            return;
        }

        RegionPlugin.getInstance().getDatabase().createRegion(new Region(
                -1,
                args[1],
                RegionPlugin.getInstance()
                        .getWandListener()
                        .getStartLocations()
                        .get(player),
                RegionPlugin.getInstance()
                        .getWandListener()
                        .getEndLocations()
                        .get(player),
                player.getUniqueId(),
                new ArrayList<>()
        ));
        player.sendMessage(RegionPlugin.getInstance().getMessage("commands.create-success"));
    }
}

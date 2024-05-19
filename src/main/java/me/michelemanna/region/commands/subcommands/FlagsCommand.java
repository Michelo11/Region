package me.michelemanna.region.commands.subcommands;

import me.michelemanna.region.RegionPlugin;
import me.michelemanna.region.commands.SubCommand;
import me.michelemanna.region.gui.FlagsMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class FlagsCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission("region.flag")) {
            player.sendMessage(RegionPlugin.getInstance().getMessage("commands.no-permission"));
            return;
        }

        if (args.length != 1) {
            player.sendMessage(RegionPlugin.getInstance().getMessage("commands.flags-usage"));
            return;
        }

        FlagsMenu.openFlags(player, RegionPlugin.getInstance().getRegionManager().getRegion(args[0]));
    }
}
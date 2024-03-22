package me.michelemanna.region.commands.subcommands;

import me.michelemanna.region.RegionPlugin;
import me.michelemanna.region.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class WhitelistCommand implements SubCommand {
    @Override
    public void execute(Player player, String[] args) {
        if (!player.hasPermission("region.whitelist")) {
            player.sendMessage(RegionPlugin.getInstance().getMessage("commands.no-permission"));
            return;
        }

        if (args.length != 2) {
            player.sendMessage(RegionPlugin.getInstance().getMessage("commands.whitelist-usage"));
            return;
        }

        if (RegionPlugin.getInstance()
                .getRegionManager()
                .getRegion(args[1]) == null) {
            player.sendMessage(RegionPlugin.getInstance().getMessage("commands.region-not-found"));
            return;
        }

        List<UUID> list = RegionPlugin.getInstance()
                        .getRegionManager()
                        .getRegion(args[1]).whitelist();

        list.stream()
                .map(Bukkit::getOfflinePlayer)
                .forEach(p -> player.sendMessage(RegionPlugin.getInstance().getMessage("commands.whitelist-list")
                        .replace("%player%", p.getName())));
    }
}

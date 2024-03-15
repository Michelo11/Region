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
            player.sendMessage("§cYou do not have permission to use this command!");
            return;
        }

        if (args.length != 2) {
            player.sendMessage("§cYou need to specify a region!");
            return;
        }

        if (RegionPlugin.getInstance()
                .getRegionManager()
                .getRegion(args[1]) == null) {
            player.sendMessage("§cThe region does not exist!");
            return;
        }

        List<UUID> list = RegionPlugin.getInstance()
                        .getRegionManager()
                        .getRegion(args[1]).whitelist();

        list.stream()
                .map(Bukkit::getOfflinePlayer)
                .forEach(p -> player.sendMessage("§a" + p.getName()));
    }
}

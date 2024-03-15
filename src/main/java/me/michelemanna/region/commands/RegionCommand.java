package me.michelemanna.region.commands;

import me.michelemanna.region.RegionPlugin;
import me.michelemanna.region.commands.subcommands.*;
import me.michelemanna.region.data.Region;
import me.michelemanna.region.gui.RegionMenu;
import me.michelemanna.region.gui.RegionsMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.item.builder.ItemBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RegionCommand implements CommandExecutor {
    private final RegionPlugin plugin;
    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public RegionCommand(RegionPlugin plugin) {
        this.plugin = plugin;
        this.subCommands.put("create", new CreateCommand());
        this.subCommands.put("delete", new DeleteCommand());
        this.subCommands.put("wand", new WandCommand());
        this.subCommands.put("add", new AddCommand());
        this.subCommands.put("remove", new RemoveCommand());
        this.subCommands.put("whitelist", new WhitelistCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cOnly players can use this command!");
            return true;
        }

        if (args.length == 0) {
            if (player.hasPermission("region.menu")) {
                RegionsMenu.openRegions(player);
                return true;
            }

            player.sendMessage("§cYou do not have permission to use this command!");
            return true;
        }

        if (subCommands.containsKey(args[0].toLowerCase())) {
            subCommands.get(args[0].toLowerCase()).execute(player, args);
            return true;
        }

        if (player.hasPermission("region.menu")) {
            Region region = plugin.getRegionManager().getRegion(args[0]);

            if (region == null) {
                player.sendMessage("§cThe region does not exist!");
                return true;
            }

            RegionMenu.openRegion(player, region);
        } else {
            player.sendMessage("§cYou do not have permission to use this command!");
        }

        return true;
    }
}
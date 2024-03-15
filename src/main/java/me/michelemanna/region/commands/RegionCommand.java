package me.michelemanna.region.commands;

import me.michelemanna.region.RegionPlugin;
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

import java.util.List;
import java.util.UUID;

public class RegionCommand implements CommandExecutor {
    private final RegionPlugin plugin;

    public RegionCommand(RegionPlugin plugin) {
        this.plugin = plugin;
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

        switch (args[0].toLowerCase()) {
            case "create" -> {
                if (!player.hasPermission("region.create")) {
                    player.sendMessage("§cYou do not have permission to use this command!");
                    return true;
                }
                
                if (args.length != 2) {
                    player.sendMessage("§cYou need to specify a name for the region!");
                    return true;
                }

                if (plugin.getWandListener().getStartLocations().get(player) == null || plugin.getWandListener().getEndLocations().get(player) == null) {
                    player.sendMessage("§cYou need to select the region with the wand first!");
                    return true;
                }

                plugin.getDatabase().createRegion(new Region(
                        -1,
                        args[1],
                        plugin.getWandListener().getStartLocations().get(player),
                        plugin.getWandListener().getEndLocations().get(player),
                        player.getUniqueId()
                ));
                player.sendMessage("§aRegion created!");
            }
            case "wand" -> {
                ItemBuilder wand = new ItemBuilder(Material.WOODEN_AXE);
                wand.setDisplayName("Region Wand");
                player.getInventory().addItem(wand.get());
                player.sendMessage("§aYou have been given the region wand!");
            }
            case "add" -> {
                if (!player.hasPermission("region.add")) {
                    player.sendMessage("§cYou do not have permission to use this command!");
                    return true;
                }
                
                if (args.length != 3) {
                    player.sendMessage("§cYou need to specify a player to add to the region!");
                    return true;
                }

                if (!plugin.getRegions().containsKey(args[1])) {
                    player.sendMessage("§cThe region does not exist!");
                    return true;
                }

                Player target = Bukkit.getPlayer(args[2]);

                if (target == null) {
                    player.sendMessage("§cThe player is not online!");
                    return true;
                }

                plugin.getDatabase().addWhitelist(plugin.getRegions().get(args[1]), target);

                player.sendMessage("§aPlayer added to the region!");
            }
            case "remove" -> {
                if (!player.hasPermission("region.remove")) {
                    player.sendMessage("§cYou do not have permission to use this command!");
                    return true;
                }

                if (args.length != 3) {
                    player.sendMessage("§cYou need to specify a player to remove from the region!");
                    return true;
                }

                if (!plugin.getRegions().containsKey(args[1])) {
                    player.sendMessage("§cThe region does not exist!");
                    return true;
                }

                Player target = Bukkit.getPlayer(args[2]);

                if (target == null) {
                    player.sendMessage("§cThe player is not online!");
                    return true;
                }

                plugin.getDatabase().removeWhiteList(plugin.getRegions().get(args[1]), target);

                player.sendMessage("§aPlayer removed from the region!");
            }
            case "whitelist" -> {
                if (!player.hasPermission("region.whitelist")) {
                    player.sendMessage("§cYou do not have permission to use this command!");
                    return true;
            }

                if (args.length != 2) {
                    player.sendMessage("§cYou need to specify a region!");
                    return true;
                }

                if (!plugin.getRegions().containsKey(args[1])) {
                    player.sendMessage("§cThe region does not exist!");
                    return true;
                }

                List<UUID> list = plugin.getDatabase().getWhitelist(plugin.getRegions().get(args[1]));
                list.stream().map(Bukkit::getOfflinePlayer).forEach(p -> player.sendMessage("§a" + p.getName()));
            }
            default -> {
                if (player.hasPermission("region.menu")) {
                    Region region = plugin.getRegions().get(args[0]);

                    if (region == null) {
                        player.sendMessage("§cThe region does not exist!");
                        return true;
                    }
    
                    RegionMenu.openRegion(player, region);
                } else {
                    player.sendMessage("§cYou do not have permission to use this command!");
                }
            }
        }

        return true;
    }
}
package me.michelemanna.region.gui.items;

import me.michelemanna.region.RegionPlugin;
import me.michelemanna.region.conversations.AddPrompt;
import me.michelemanna.region.data.Region;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class WhiteListAddItem extends AbstractItem {
    private final Region region;

    public WhiteListAddItem(Region region) {
        this.region = region;
    }

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.EMERALD)
                .setDisplayName("Whitelist Add");
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        if (!player.hasPermission("region.add")) {
            player.sendMessage("§cYou do not have permission to use this command!");
            return;
        }

        player.closeInventory();

        new ConversationFactory(RegionPlugin.getInstance())
                .withEscapeSequence("cancel")
                .withFirstPrompt(new AddPrompt(RegionPlugin.getInstance(), region))
                .withModality(false)
                .buildConversation(player)
                .begin();
    }
}

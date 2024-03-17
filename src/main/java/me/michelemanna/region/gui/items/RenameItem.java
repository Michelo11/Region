package me.michelemanna.region.gui.items;

import me.michelemanna.region.RegionPlugin;
import me.michelemanna.region.conversations.RenamePrompt;
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

public class RenameItem extends AbstractItem {
    private final Region region;

    public RenameItem(Region region) {
        this.region = region;
    }

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.PAPER)
                .setDisplayName("Rename");
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        player.closeInventory();

        new ConversationFactory(RegionPlugin.getInstance())
                .withEscapeSequence("cancel")
                .withFirstPrompt(new RenamePrompt(region))
                .withModality(false)
                .buildConversation(player)
                .begin();
    }
}
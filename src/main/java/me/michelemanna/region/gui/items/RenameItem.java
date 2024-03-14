package me.michelemanna.region.gui.items;

import me.michelemanna.region.RegionPlugin;
import me.michelemanna.region.data.Region;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
        Prompt prompt = new StringPrompt() {
            @NotNull
            @Override
            public String getPromptText(@NotNull ConversationContext context) {
                return "Enter the new name for the region";
            }

            @Nullable
            @Override
            public Prompt acceptInput(@NotNull ConversationContext context, @Nullable String input) {
                if (input == null) {
                    return this;
                }

                RegionPlugin.getInstance().getDatabase().updateName(region, input);

                player.sendMessage("§aRegion renamed to " + input);

                return null;
            }
        };

        new ConversationFactory(RegionPlugin.getInstance())
                .withEscapeSequence("cancel")
                .withFirstPrompt(prompt)
                .withModality(false)
                .buildConversation(player)
                .begin();
    }
}
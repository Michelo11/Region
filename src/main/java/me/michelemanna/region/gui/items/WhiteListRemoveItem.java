package me.michelemanna.region.gui.items;

import me.michelemanna.region.RegionPlugin;
import me.michelemanna.region.data.Region;
import org.bukkit.Material;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.PlayerNamePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class WhiteListRemoveItem extends AbstractItem {
    private final Region region;

    public WhiteListRemoveItem(Region region) {
        this.region = region;
    }

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(Material.REDSTONE)
                .setDisplayName("Whitelist Remove");
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
        if (!player.hasPermission("region.remove")) {
            player.sendMessage("§cYou do not have permission to use this command!");
            return;
        }

        player.closeInventory();

        Prompt prompt = new PlayerNamePrompt(RegionPlugin.getInstance()) {
            @Nullable
            @Override
            protected Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull Player input) {
                RegionPlugin.getInstance().getDatabase().removeWhiteList(region, input);

                player.sendMessage("§cPlayer removed from the whitelist");

                return null;
            }

            @NotNull
            @Override
            public String getPromptText(@NotNull ConversationContext context) {
                return "Enter the name of the player to remove from the whitelist";
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

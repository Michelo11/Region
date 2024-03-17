package me.michelemanna.region.conversations;

import me.michelemanna.region.RegionPlugin;
import me.michelemanna.region.data.Region;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.PlayerNamePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RemovePrompt extends PlayerNamePrompt {
    private final Region region;

    public RemovePrompt(@NotNull Plugin plugin, Region region) {
        super(plugin);
        this.region = region;
    }

    @Nullable
    @Override
    protected Prompt acceptValidatedInput(@NotNull ConversationContext context, @NotNull Player input) {
        RegionPlugin.getInstance().getDatabase().removeWhiteList(region, input);

        ((Player) context.getForWhom()).sendMessage("§cPlayer removed from the whitelist");

        return null;
    }

    @NotNull
    @Override
    public String getPromptText(@NotNull ConversationContext context) {
        return "Enter the name of the player to remove from the whitelist";
    }
}

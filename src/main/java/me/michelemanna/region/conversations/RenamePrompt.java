package me.michelemanna.region.conversations;

import me.michelemanna.region.RegionPlugin;
import me.michelemanna.region.data.Region;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RenamePrompt extends StringPrompt {
    private final Region region;

    public RenamePrompt(Region region) {
        this.region = region;
    }

    @NotNull
    @Override
    public String getPromptText(@NotNull ConversationContext context) {
        return RegionPlugin.getInstance().getMessage("conversations.rename-prompt");
    }

    @Nullable
    @Override
    public Prompt acceptInput(@NotNull ConversationContext context, @Nullable String input) {
        if (input == null) {
            return this;
        }

        RegionPlugin.getInstance().getDatabase().updateName(region, input);

        ((Player) context.getForWhom()).sendMessage(RegionPlugin.getInstance().getMessage("conversations.rename-success")
        .replace("%region%", input));

        return null;
    }
}

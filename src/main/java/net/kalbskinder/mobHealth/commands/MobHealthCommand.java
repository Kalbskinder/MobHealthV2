package net.kalbskinder.mobHealth.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kalbskinder.mobHealth.configuration.Config;
import net.kalbskinder.mobHealth.enums.DisplaySetting;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

public class MobHealthCommand {

    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    public static LiteralCommandNode<CommandSourceStack> mobHealthCommand() {
        return Commands.literal("mobhealth")
                .requires(ctx -> ctx.getExecutor() instanceof Player player && player.hasPermission("mobhealth.command"))
                .then(Commands.argument("barSetting", StringArgumentType.string())
                        .suggests(MobHealthCommand::getBarSettingSuggestions)
                        .executes(ctx -> {
                            String settingName = StringArgumentType.getString(ctx, "barSetting").toUpperCase();
                            if (ctx.getSource().getExecutor() instanceof Player player) {
                                player.sendMessage(miniMessage.deserialize("<green>Selected bar setting: <yellow>" + settingName));
                                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.8f);
                                Config.updateSelectedProfile(DisplaySetting.valueOf(settingName));
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                )
                .build();

    }

    private static CompletableFuture<Suggestions> getBarSettingSuggestions(final CommandContext<CommandSourceStack> ctx, final SuggestionsBuilder builder) {
        for (DisplaySetting setting : DisplaySetting.values()) {
            builder.suggest(setting.name());
        }
        return builder.buildFuture();
    }
}

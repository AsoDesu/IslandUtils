package net.asodev.islandutils.util;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.asodev.islandutils.IslandUtils;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.modules.crafting.state.CraftingItems;
import net.asodev.islandutils.modules.crafting.state.CraftingNotifier;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class IslandUtilsCommand {
    private static final Component cantUseDebugError =
            Component.literal(ChatUtils.translate("You must be in debug mode to use the debug command."));

    public static LiteralArgumentBuilder<FabricClientCommandSource> craftsCommand = literal("crafting")
            .executes(ctx -> {
                ctx.getSource().sendFeedback(CraftingNotifier.activeCraftsMessage());
               return 0;
            });
    public static LiteralArgumentBuilder<FabricClientCommandSource> debugSubcommand = literal("debug")
            .then(literal("add_craft")
            .then(argument("color", StringArgumentType.string())
            .then(argument("slot", IntegerArgumentType.integer())
            .then(argument("delay", IntegerArgumentType.integer())
            .executes(ctx -> {
                if (!IslandOptions.getMisc().isDebugMode()) {
                    ctx.getSource().sendError(cantUseDebugError);
                    return 0;
                }
                String color = ctx.getArgument("color", String.class);
                Integer slot = ctx.getArgument("slot", Integer.class);
                Integer delay = ctx.getArgument("delay", Integer.class);
                CraftingItems.addDebugItem(color, slot, delay);
                return 1;
            })))));
    public static LiteralArgumentBuilder<FabricClientCommandSource> islandCommand = literal("islandutils")
            .then(craftsCommand);

    public static void register() {
        if (IslandUtils.isPreRelease()) {
            islandCommand = islandCommand.then(debugSubcommand);
        }

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(islandCommand);
            dispatcher.register(craftsCommand);
        });
    }

}

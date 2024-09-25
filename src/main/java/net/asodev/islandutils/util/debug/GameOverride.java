package net.asodev.islandutils.util.debug;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.asodev.islandutils.state.Game;
import net.asodev.islandutils.state.MccIslandState;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class GameOverride {

    public static LiteralArgumentBuilder<FabricClientCommandSource> getDebugCommand() {
        return literal("override_game")
            .then(argument("game", StringArgumentType.greedyString())
            .executes(ctx -> {
                String game = ctx.getArgument("game", String.class);
                Game gameGame = Game.valueOf(game.toUpperCase());
                MccIslandState.setGame(gameGame);
                return 1;
            }));
    }

}

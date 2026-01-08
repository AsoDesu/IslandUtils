package dev.asodesu.islandutils.api.game

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import com.noxcrew.noxesium.core.mcc.ClientboundMccServerPacket
import dev.asodesu.islandutils.api.extentions.debugMode
import java.util.concurrent.CompletableFuture
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource

fun gamesDebugCommand() = literal("games")
    .then(forceGameDebugCommand())

fun gameTypeSuggestions(ctx: CommandContext<FabricClientCommandSource>, builder: SuggestionsBuilder): CompletableFuture<Suggestions> {
    gameManager.gamesById.keys.forEach { builder.suggest(it) }
    return CompletableFuture.completedFuture(builder.build())
}

private fun forceGameDebugCommand() = literal("set_game")
    .then(argument("game_type", StringArgumentType.word()).suggests(::gameTypeSuggestions)
    .executes { ctx ->
        if (!debugMode) return@executes 0

        val gameType = ctx.getArgument("game_type", String::class.javaObjectType)
        val gameContext = gameManager.gamesById[gameType] ?: return@executes 0
        gameManager.setGame(
            gameContext.create(ClientboundMccServerPacket("", listOf("barren_3", "main-1")))
        )

        1
    })
package dev.asodesu.islandutils

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import dev.asodesu.islandutils.features.crafting.craftingCommand
import dev.asodesu.islandutils.features.crafting.craftingDebugCommand
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.fabricmc.loader.api.FabricLoader

fun islandUtilsCommand(): LiteralArgumentBuilder<FabricClientCommandSource>? {
    var command = literal("islandutils")
        .then(craftingCommand())
    if (FabricLoader.getInstance().isDevelopmentEnvironment) command = command.then(debugCommand())
    return command
}

fun debugCommand() = literal("debug")
    .then(craftingDebugCommand())
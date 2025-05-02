package dev.asodesu.islandutils.api

import dev.asodesu.islandutils.options.MiscOptions
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import org.slf4j.LoggerFactory

val debugMode by MiscOptions.Debug.debugMode

private val logger = LoggerFactory.getLogger("IU-DEBUG")
fun debug(str: String) {
    if (debugMode) send(Component.literal("[IslandUtils] $str").withStyle(ChatFormatting.GRAY))
    else logger.info(str)
}
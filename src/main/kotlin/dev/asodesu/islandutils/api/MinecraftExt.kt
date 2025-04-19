package dev.asodesu.islandutils.api

import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientPacketListener
import net.minecraft.client.player.LocalPlayer

/**
 * The current minecraft client instance
 */
val minecraft: Minecraft
    get() = Minecraft.getInstance()

/**
 * The current local player (or null if the player is not connected to a world)
 */
val player: LocalPlayer?
    get() = minecraft.player

/**
 * The current active server connection
 */
val connection: ClientPacketListener?
    get() = minecraft.connection

private val mcciDomains = listOf(
    "mccisland.net",
    "mccisland.com"
)

/**
 * If the player is currently online on MCC Island
 */
val isOnline: Boolean
    get() {
        val ip = minecraft.currentServer?.ip?.lowercase() ?: return false
        return mcciDomains.any { ip.contains(it) }
    }
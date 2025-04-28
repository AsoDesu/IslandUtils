package dev.asodesu.islandutils.api

import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientPacketListener
import net.minecraft.client.multiplayer.chat.ChatListener
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent

/**
 * The current minecraft client instance
 */
val minecraft: Minecraft
    get() = Minecraft.getInstance()

/**
 * Minecraft Chat Listener
 */
val chatListener: ChatListener
    get() = minecraft.chatListener

/**
 * Sends a chat message to player
 */
fun send(component: Component) {
    chatListener.handleSystemMessage(component, false)
}

object Resources {
    fun islandUtils(path: String) = ResourceLocation.fromNamespaceAndPath("islandutils", path)
    fun mcc(path: String) = ResourceLocation.fromNamespaceAndPath("mcc", path)
}

fun ResourceLocation.toSoundEvent() = SoundEvent.createVariableRangeEvent(this)

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
package dev.asodesu.islandutils.api.extentions

import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientPacketListener
import net.minecraft.client.multiplayer.chat.ChatListener
import net.minecraft.client.resources.sounds.SimpleSoundInstance
import net.minecraft.client.sounds.SoundManager
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
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
    fun islandUtils(path: String): Identifier = Identifier.fromNamespaceAndPath("islandutils", path)
    fun mcc(path: String): Identifier = Identifier.fromNamespaceAndPath("mcc", path)
}

fun Identifier.toSoundEvent() = SoundEvent.createVariableRangeEvent(this)
fun SoundManager.play(identifier: Identifier, volume: Float = 1f, pitch: Float = 1f) = this.play(identifier.toSoundEvent(), volume, pitch)
fun SoundManager.play(event: SoundEvent, volume: Float = 1f, pitch: Float = 1f) = this.play(SimpleSoundInstance.forUI(event, pitch, volume))

/**
 * The current active server connection
 */
val connection: ClientPacketListener?
    get() = minecraft.connection

private val mcciDomains = listOf(
    "mccisland.net",
    "mccisland.com"
)

fun isMccIp(ip: String?): Boolean {
    if (ip == null) return false
    val lowercase = ip.lowercase()
    return mcciDomains.any { lowercase.contains(it) }
}

/**
 * If the player is currently online on MCC Island
 */
val isOnline: Boolean get() = isMccIp(minecraft.currentServer?.ip)
package dev.asodesu.islandutils.api

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.minimessage.MiniMessage

/**
 * The MiniMessage serializer
 */
val miniMessage = MiniMessage.miniMessage()

/**
 * The local player audience (or empty if the player isn't connected to a server)
 */
val audience: Audience
    get() = player as? Audience ?: Audience.empty()

/**
 * Deserializes a MiniMessage string to an adventure component
 *
 * @param str A MiniMessage string
 * @return The component from this MiniMessage string
 */
fun miniMessage(str: String) = miniMessage.deserialize(str)

/**
 * Sends a message to an audience using MiniMessage
 *
 * @param str MiniMessage string
 */
fun Audience.send(str: String) {
    audience.sendMessage(miniMessage(str))
}
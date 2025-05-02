package dev.asodesu.islandutils.api.json

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.modules.SerializersModule
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

val JSON = Json {
    serializersModule = SerializersModule {
        contextual(Component::class, ComponentSerializer)
        contextual(ItemStack::class, ItemStackSerializer)
    }
    ignoreUnknownKeys = true
}

inline fun <reified T> T.encode() = JSON.encodeToString(this)
inline fun <reified T> T.encodeToJsonElement() = JSON.encodeToJsonElement(this)

inline fun <reified T> String.decode(): T = JSON.decodeFromString<T>(this)
inline fun <reified T> JsonElement.decodeFromJsonElement(): T = JSON.decodeFromJsonElement<T>(this)
package dev.asodesu.islandutils.api.json

import com.mojang.serialization.Codec
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization

object ComponentSerializer : JsonCodecSerializer<Component>() {
    override val codec: Codec<Component> = ComponentSerialization.CODEC
}
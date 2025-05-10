package dev.asodesu.islandutils.api.json

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.util.GsonHelper

abstract class JsonCodecSerializer<T> : KSerializer<T> {
    override val descriptor: SerialDescriptor = String.serializer().descriptor
    abstract val codec: Codec<T>
    private val gson = Gson()

    override fun deserialize(decoder: Decoder): T {
        val jsonElement = GsonHelper.fromJson(gson, decoder.decodeString(), JsonElement::class.java)
        val dataResult = codec.decode(JsonOps.INSTANCE, jsonElement)
        return dataResult.orThrow.first
    }

    override fun serialize(encoder: Encoder, value: T) {
        val jsonElement = codec.encodeStart(JsonOps.INSTANCE, value).orThrow
        encoder.encodeString(jsonElement.toString())
    }
}
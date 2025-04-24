package dev.asodesu.islandutils.api.options

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectBuilder
import net.minecraft.client.gui.layouts.LayoutElement

interface ConfigEntry {
    fun load(json: JsonObject)
    fun save(json: JsonObjectBuilder)

    fun render(): LayoutElement
}
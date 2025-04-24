package dev.asodesu.islandutils.api.options

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectBuilder

interface ConfigEntry {
    fun load(json: JsonObject)
    fun save(json: JsonObjectBuilder)
}
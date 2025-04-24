package dev.asodesu.islandutils.api.options.option

import dev.asodesu.islandutils.api.options.ConfigEntry
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.encodeToJsonElement
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class Option<T>(
    val name: String,
    private val default: T,
    private val serializer: KSerializer<T>,
    private val hasDescription: Boolean
) : ReadOnlyProperty<Any, T>, ConfigEntry {
    var value = default

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return value
    }

    override fun load(json: JsonObject) {
        val element = json[name] ?: return
        val obj = Json.decodeFromJsonElement(serializer, element)
        this.value = obj
    }

    override fun save(json: JsonObjectBuilder) {
        val element = Json.encodeToJsonElement(serializer, value)
        json.put(name, element)
    }
}
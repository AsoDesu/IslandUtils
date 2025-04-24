package dev.asodesu.islandutils.api.options.option

import dev.asodesu.islandutils.api.options.ConfigEntry
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.encodeToJsonElement
import net.minecraft.client.gui.layouts.LayoutElement
import net.minecraft.network.chat.Component
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class Option<T>(
    private val name: String,
    private val default: T,
    private val serializer: KSerializer<T>,
    private val renderer: OptionRenderer<T>,
    private val hasDescription: Boolean
) : ReadOnlyProperty<Any, T>, ConfigEntry {
    val component = Component.translatable("islandutils.options.$name")
    var value = default

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return value
    }

    override fun render() = renderer.render(this)

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
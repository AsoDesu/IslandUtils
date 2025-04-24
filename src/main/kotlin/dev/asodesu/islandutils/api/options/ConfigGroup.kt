package dev.asodesu.islandutils.api.options

import dev.asodesu.islandutils.api.options.option.Option
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectBuilder

abstract class ConfigGroup(val name: String) : ConfigEntry {
    private val children = mutableListOf<ConfigEntry>()

    fun <T> option(name: String, def: T, serializer: KSerializer<T>, desc: Boolean = false): Option<T> {
        return Option(name, def, serializer, desc)
            .also { children += it }
    }

    fun toggle(name: String, def: Boolean, desc: Boolean = false)
        = option(name, def, Boolean.serializer(), desc)

    fun group(group: ConfigGroup) {
        children += group
    }

    override fun load(json: JsonObject) {
        children.forEach { it.load(json) }
    }

    override fun save(json: JsonObjectBuilder) {
        children.forEach { it.save(json) }
    }
}
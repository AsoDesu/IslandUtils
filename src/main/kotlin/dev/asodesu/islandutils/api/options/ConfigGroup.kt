package dev.asodesu.islandutils.api.options

import dev.asodesu.islandutils.api.options.option.Option
import dev.asodesu.islandutils.api.options.option.OptionRenderer
import dev.asodesu.islandutils.api.options.option.ToggleOptionRenderer
import dev.asodesu.islandutils.api.options.screen.tab.ConfigGroupLayout
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectBuilder
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.client.gui.layouts.LayoutElement
import net.minecraft.network.chat.Component

abstract class ConfigGroup(val name: String) : ConfigEntry {
    val component: Component = Component.translatable("islandutils.options.$name")
    private val children = mutableListOf<ConfigEntry>()

    protected fun <T> option(name: String, def: T, serializer: KSerializer<T>, renderer: OptionRenderer<T>, desc: Boolean = false): Option<T> {
        return Option(name, def, serializer, renderer, desc)
            .also { children += it }
    }

    protected fun toggle(name: String, def: Boolean, desc: Boolean = false, renderer: OptionRenderer<Boolean> = ToggleOptionRenderer)
        = option(name, def, Boolean.serializer(), renderer, desc)

    protected fun group(group: ConfigGroup) {
        children += group
    }

    override fun render(layout: Layout): LayoutElement = ConfigGroupLayout(this)
    fun children() = children as List<ConfigEntry>

    override fun load(json: JsonObject) {
        children.forEach { it.load(json) }
    }

    override fun save(json: JsonObjectBuilder) {
        children.forEach { it.save(json) }
    }
}
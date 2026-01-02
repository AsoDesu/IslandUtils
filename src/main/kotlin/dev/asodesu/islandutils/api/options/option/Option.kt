package dev.asodesu.islandutils.api.options.option

import dev.asodesu.islandutils.api.extentions.appendLine
import dev.asodesu.islandutils.api.extentions.buildComponent
import dev.asodesu.islandutils.api.extentions.copyAndStyle
import dev.asodesu.islandutils.api.extentions.newLine
import dev.asodesu.islandutils.api.options.ConfigEntry
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectBuilder
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.Tooltip
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.network.chat.Component
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class Option<T>(
    private val name: String,
    private val default: T,
    private val serializer: KSerializer<T>,
    private var renderer: OptionRenderer<T>,
    private val hasDescription: Boolean
) : ReadOnlyProperty<Any?, T>, ConfigEntry {
    val component = Component.translatable("islandutils.options.$name")
    private val descriptionComponent = Component.translatable("islandutils.options.$name.desc")
        .withStyle(ChatFormatting.DARK_AQUA)
    private var value = default
    private var onChange: MutableList<((T, T) -> Unit)> = mutableListOf()

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value
    }

    fun onChange(func: (T, T) -> Unit) = apply {
        this.onChange += func
    }
    fun withRenderer(renderer: OptionRenderer<T>) = apply {
        this.renderer = renderer
    }

    fun get() = value
    fun set(newValue: T) {
        val oldValue = this.value
        this.value = newValue
        this.onChange.forEach { it(oldValue, newValue) }
    }

    fun renderer() = renderer
    override fun render(layout: Layout) = renderer.render(this, layout).also { widget ->

        val tooltip = Tooltip.create(buildComponent {
            appendLine(component.copyAndStyle { withBold(true) })
            newLine()
            append(descriptionComponent)
        })
        if (widget is AbstractWidget) {
            widget.setTooltip(tooltip)
        } else if (widget is Layout) {
            widget.visitWidgets { it.setTooltip(tooltip) }
        }
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
package dev.asodesu.islandutils.api.sidebar

import dev.asodesu.islandutils.api.debug
import dev.asodesu.islandutils.api.events.EventKey
import net.minecraft.network.chat.Component
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class SidebarLineDelegate(key: EventKey? = null, regex: String) : ReadOnlyProperty<Any?, String?> {
    private val pattern = Regex(regex)
    private var value: String? = null

    init {
        SidebarEvents.LINE_UPDATE.register(key, ::handleLineUpdate)
    }

    private fun handleLineUpdate(line: Component) {
        val plainString = line.string
        val result = pattern.find(plainString) ?: return
        value = result.groupValues.getOrNull(1) ?: ""
        debug("Got sidebar update containing '$value'")
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>) = value
}
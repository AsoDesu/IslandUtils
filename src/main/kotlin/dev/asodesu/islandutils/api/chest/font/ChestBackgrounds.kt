package dev.asodesu.islandutils.api.chest.font

import dev.asodesu.islandutils.api.extentions.Resources
import java.util.*
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.FontDescription
import net.minecraft.network.chat.Style
import net.minecraft.resources.Identifier

object ChestBackgrounds : FontCollection.Font {
    val FONT_KEY = FontDescription.Resource(Resources.mcc("chest_backgrounds"))
    private val CHEST_BACKGROUND_STYLE = Style.EMPTY.withFont(FONT_KEY).withColor(ChatFormatting.WHITE)
    private val menuComponents = mutableMapOf<Component, Identifier>()

    override fun add(file: Identifier, character: String) {
        val component = Component.literal(character).withStyle(CHEST_BACKGROUND_STYLE)
        menuComponents[component] = file
    }

    override fun clear() = menuComponents.clear()

    fun get(component: Component): Collection<Identifier> {
        val flatComponent = component.toFlatList()
        return menuComponents.filter { Collections.indexOfSubList(flatComponent, listOf(it.key)) != -1 }.values
    }
}
package dev.asodesu.islandutils.api.chest.font

import dev.asodesu.islandutils.api.Resources
import java.util.*
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.resources.ResourceLocation

object ChestBackgrounds : FontCollection.Font {
    val FONT_KEY = Resources.mcc("chest_backgrounds")
    private val CHEST_BACKGROUND_STYLE = Style.EMPTY.withFont(FONT_KEY).withColor(ChatFormatting.WHITE)
    private val menuComponents = mutableMapOf<Component, ResourceLocation>()

    override fun add(file: ResourceLocation, character: String) {
        val component = Component.literal(character).withStyle(CHEST_BACKGROUND_STYLE)
        menuComponents[component] = file
    }

    override fun clear() = menuComponents.clear()

    fun get(component: Component): Collection<ResourceLocation> {
        val flatComponent = component.toFlatList()
        return menuComponents.filter { Collections.indexOfSubList(flatComponent, listOf(it.key)) != -1 }.values
    }
}
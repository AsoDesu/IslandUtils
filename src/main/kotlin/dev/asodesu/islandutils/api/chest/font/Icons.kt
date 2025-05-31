package dev.asodesu.islandutils.api.chest.font

import dev.asodesu.islandutils.api.extentions.Resources
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.resources.ResourceLocation

object Icons : FontCollection.Font {
    val FONT_KEY = Resources.mcc("icon")
    val ICON_STYLE = Style.EMPTY.withFont(FONT_KEY).withColor(ChatFormatting.WHITE)
    private val characterToFileMap = mutableMapOf<ResourceLocation, String>()

    override fun add(file: ResourceLocation, character: String) {
        characterToFileMap[file] = character
    }

    fun getCharacter(file: ResourceLocation) = characterToFileMap[file]
    fun toIcon(character: String) = Component.literal(character).withStyle(ICON_STYLE)

    override fun clear() {
        characterToFileMap.clear()
    }
}
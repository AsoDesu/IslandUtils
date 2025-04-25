package dev.asodesu.islandutils

import dev.asodesu.islandutils.api.Resources
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style

object Font {
    private val ICONS = Resources.islandUtils("icons")
    private val ICONS_STYLE = Style.EMPTY.withFont(ICONS).withColor(ChatFormatting.WHITE)

    val SOCIAL_ICON = icon("\ue001")

    private fun icon(char: String): Component {
        return Component.literal(char).withStyle(ICONS_STYLE)
    }
}
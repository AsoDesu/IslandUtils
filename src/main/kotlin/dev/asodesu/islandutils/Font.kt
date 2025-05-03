package dev.asodesu.islandutils

import dev.asodesu.islandutils.api.extentions.Resources
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style

object Font {
    val HUD = Resources.mcc("hud")
    val HUD_STYLE = Style.EMPTY.withFont(HUD)

    val ICONS = Resources.islandUtils("icons")
    val ICONS_STYLE = Style.EMPTY.withFont(ICONS).withColor(ChatFormatting.WHITE)

    val SOCIAL_ICON = icon("\ue001")
    val FUSION_ICON = icon("\ue002")
    val ASSEMBLER_ICON = icon("\ue003")

    private fun icon(char: String): Component {
        return Component.literal(char).withStyle(ICONS_STYLE)
    }
}
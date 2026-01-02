package dev.asodesu.islandutils

import dev.asodesu.islandutils.api.extentions.Resources
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.FontDescription
import net.minecraft.network.chat.Style

object Font {
    val HUD = Resources.mcc("hud")
    val HUD_STYLE = Style.EMPTY.withFont(FontDescription.Resource(HUD))

    val ICONS = Resources.islandUtils("icons")
    val ICONS_STYLE = Style.EMPTY.withFont(FontDescription.Resource(ICONS)).withColor(ChatFormatting.WHITE)

    val SOCIAL_ICON = icon("\ue001")
    val FUSION_ICON = icon("\ue002")
    val ASSEMBLER_ICON = icon("\ue003")
    val ACTION_CLICK_LEFT = icon("\ue004")
    val ACTION_CLICK_RIGHT = icon("\ue005")
    val HAT_BADGE = icon("\ue006")
    val ACCESSORY_BADGE = icon("\ue007")
    val CLOAK_BADGE = icon("\ue008")
    val ROD_BADGE = icon("\ue009")

    private fun icon(char: String): Component {
        return Component.literal(char).withStyle(ICONS_STYLE)
    }
}
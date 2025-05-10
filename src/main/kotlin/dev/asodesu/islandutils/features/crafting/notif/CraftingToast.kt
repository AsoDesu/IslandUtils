package dev.asodesu.islandutils.features.crafting.notif

import dev.asodesu.islandutils.api.extentions.Resources
import dev.asodesu.islandutils.api.extentions.pose
import dev.asodesu.islandutils.features.crafting.items.CraftingItem
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.toasts.Toast
import net.minecraft.client.gui.components.toasts.ToastManager
import net.minecraft.client.renderer.RenderType
import net.minecraft.network.chat.Component
import net.minecraft.util.ARGB
import dev.asodesu.islandutils.Font as MccFont

class CraftingToast(private val craftingItem: CraftingItem) : Toast {
    private val BACKGROUND_SPRITE = Resources.islandUtils("toast/crafting_toast")
    private val VISIBLE_FOR = 5000L
    private val TITLE_TEXT = Component.translatable("islandutils.feature.crafting.complete")
        .withStyle(MccFont.HUD_STYLE.withColor(ChatFormatting.WHITE))

    private val TOP_PADDING = 7
    private val HUD_FONT_HEIGHT = 5
    private val TEXT_GAP = 4
    private val ITEM_HEIGHT = 16

    private var visibility: Toast.Visibility = Toast.Visibility.HIDE

    override fun getWantedVisibility() = visibility
    override fun update(toastManager: ToastManager, l: Long) {
        val visibleFor = VISIBLE_FOR * toastManager.notificationDisplayTimeMultiplier
        visibility = if (l > visibleFor) Toast.Visibility.HIDE else Toast.Visibility.SHOW
    }

    override fun render(guiGraphics: GuiGraphics, font: Font, l: Long) {
        guiGraphics.blitSprite(RenderType::guiTextured, BACKGROUND_SPRITE, 0, 0, this.width(), this.height())

        var y = TOP_PADDING
        guiGraphics.drawString(font, TITLE_TEXT, 30, y, ARGB.white(1f), false)
        y += HUD_FONT_HEIGHT + TEXT_GAP
        guiGraphics.drawString(font, craftingItem.item.hoverName, 30, y, ARGB.white(1f), false)

        guiGraphics.renderFakeItem(craftingItem.item, 8, 8)

        guiGraphics.pose {
            translate(0.0, 0.0, 200.0)
            guiGraphics.drawString(font, craftingItem.type.icon, 6, 8 + ITEM_HEIGHT - 6, ARGB.white(1f), false)
        }
    }
}
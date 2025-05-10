package dev.asodesu.islandutils.api.notifier

import dev.asodesu.islandutils.api.extentions.isMccIp
import dev.asodesu.islandutils.api.extentions.minecraft
import dev.asodesu.islandutils.options.NotificationOptions
import java.util.*
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList.OnlineServerEntry
import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.ResourceLocation

object ServerListNotificationRenderer {
    private val enabled by NotificationOptions.showInServerMenu
    private val NOTIFICATION_SPRITE = ResourceLocation.withDefaultNamespace("icon/unseen_notification")
    private val SERVER_ICON_SIZE = 32 // size of the server icon in minecraft

    private val ICON_SIZE = 10
    private val ICON_PADDING_X = 5

    fun render(guiGraphics: GuiGraphics, entry: OnlineServerEntry, index: Int, y: Int, x: Int, entryWidth: Int, entryHeight: Int, mouseX: Int, mouseY: Int) {
        if (!enabled || !isMccIp(entry.serverData.ip)) return
        if (Notifier.isEmpty()) return

        val iconX = x - ICON_SIZE - 5
        val iconBoundX = iconX + ICON_PADDING_X
        val iconY = y + ((SERVER_ICON_SIZE - ICON_SIZE) / 2)
        val iconBoundY = iconY + ICON_SIZE

        // check if we're mousing over the notification
        if (mouseX >= iconX && mouseY >= iconY && mouseX <= iconBoundX && mouseY <= iconBoundY) {
            guiGraphics.renderTooltip(minecraft.font, Notifier.get(), Optional.empty(), mouseX, mouseY)
        }

        guiGraphics.blitSprite(RenderType::guiTextured, NOTIFICATION_SPRITE, iconX, iconY, ICON_SIZE, ICON_SIZE)
    }

}
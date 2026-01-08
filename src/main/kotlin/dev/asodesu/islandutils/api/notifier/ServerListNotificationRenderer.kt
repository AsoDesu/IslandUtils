package dev.asodesu.islandutils.api.notifier

import dev.asodesu.islandutils.api.extentions.isInsideBox
import dev.asodesu.islandutils.api.extentions.minecraft
import dev.asodesu.islandutils.api.server.ServerSessionHandler
import dev.asodesu.islandutils.options.NotificationOptions
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList.OnlineServerEntry
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.Identifier

object ServerListNotificationRenderer {
    private val enabled by NotificationOptions.showInServerMenu
    private val NOTIFICATION_SPRITE = Identifier.withDefaultNamespace("icon/unseen_notification")
    private val SERVER_ICON_SIZE = 32 // size of the server icon in minecraft

    private val ICON_SIZE = 10
    private val ICON_PADDING_X = 5

    fun render(guiGraphics: GuiGraphics, entry: OnlineServerEntry, y: Int, x: Int, mouseX: Int, mouseY: Int) {
        if (!enabled || !ServerSessionHandler.isIslandServer(entry.serverData)) return
        if (Notifier.isEmpty()) return

        val iconX = x - ICON_SIZE - ICON_PADDING_X
        val iconY = y + ((SERVER_ICON_SIZE - ICON_SIZE) / 2)

        // check if we're mousing over the notification
        if (isInsideBox(mouseX, mouseY, iconX, iconY, ICON_SIZE, ICON_SIZE)) {
            guiGraphics.setComponentTooltipForNextFrame(minecraft.font, Notifier.get(), mouseX, mouseY)
        }

        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, NOTIFICATION_SPRITE, iconX, iconY, ICON_SIZE, ICON_SIZE)
    }

}
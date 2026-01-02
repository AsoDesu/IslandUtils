package dev.asodesu.islandutils.features.chatbuttons.button

import dev.asodesu.islandutils.features.chatbuttons.ChatButtons
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.Identifier

class SpriteChannelButton(override val channel: String, private val sprite: Identifier) : ChannelButton {
    override fun widget() = Widget(this, sprite)

    class Widget(
        override val channelButton: SpriteChannelButton,
        private val sprite: Identifier,
        x: Int = 0,
        y: Int = 0,
        width: Int = ChatButtons.BUTTON_WIDTH,
        height: Int = ChatButtons.BUTTON_HEIGHT
    ) : ChatButtonWidget(x, y, width, height) {
        override fun renderContents(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, this.x, this.y, this.width, this.height)
            this.checkAndRenderUnderline(guiGraphics)
        }
    }
}
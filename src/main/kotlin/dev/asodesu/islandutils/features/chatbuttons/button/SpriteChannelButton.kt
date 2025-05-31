package dev.asodesu.islandutils.features.chatbuttons.button

import dev.asodesu.islandutils.features.chatbuttons.ChatButtons
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.ResourceLocation

class SpriteChannelButton(override val channel: String, private val sprite: ResourceLocation) : ChannelButton {
    override fun widget() = Widget(this, sprite)

    class Widget(
        override val channelButton: SpriteChannelButton,
        private val sprite: ResourceLocation,
        x: Int = 0,
        y: Int = 0,
        width: Int = ChatButtons.BUTTON_WIDTH,
        height: Int = ChatButtons.BUTTON_HEIGHT
    ) : ChatButtonWidget(x, y, width, height) {
        override fun renderWidget(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
            guiGraphics.blitSprite(RenderType::guiTextured, sprite, this.x, this.y, this.width, this.height)
            this.checkAndRenderUnderline(guiGraphics)
        }
    }
}
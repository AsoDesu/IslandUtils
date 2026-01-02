package dev.asodesu.islandutils.features.chatbuttons.button

import dev.asodesu.islandutils.api.extentions.minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.util.ARGB

class ComponentChannelButton(override val channel: String, private val component: Component) : ChannelButton {
    override fun widget() = Widget(this, component)

    class Widget(
        override val channelButton: ComponentChannelButton,
        private val component: Component,
        x: Int = 0,
        y: Int = 0,
        width: Int = 43,
        height: Int = 9
    ) : ChatButtonWidget(x, y, width, height) {
        private val font = minecraft.font
        private val tintColour = ARGB.white(1f)

        override fun renderContents(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
            guiGraphics.drawString(font, component, this.x, this.y, tintColour, false)
            this.checkAndRenderUnderline(guiGraphics)
        }
    }
}
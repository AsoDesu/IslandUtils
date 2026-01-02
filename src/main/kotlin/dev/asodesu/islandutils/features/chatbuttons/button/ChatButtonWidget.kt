package dev.asodesu.islandutils.features.chatbuttons.button

import dev.asodesu.islandutils.api.extentions.rect
import dev.asodesu.islandutils.features.chatbuttons.ChatButtons
import net.minecraft.client.gui.ComponentPath
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractButton
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.gui.navigation.FocusNavigationEvent
import net.minecraft.client.input.InputWithModifiers
import net.minecraft.network.chat.Component

abstract class ChatButtonWidget(
    x: Int,
    y: Int,
    width: Int,
    height: Int,
    component: Component = Component.empty()
) : AbstractButton(x, y, width, height, component) {
    abstract val channelButton: ChannelButton

    protected fun checkAndRenderUnderline(guiGraphics: GuiGraphics) {
        if (isHoveredOrFocused) guiGraphics.rect(this.x, this.y + this.height - 1, this.width, 1, ChatButtons.UNDERLINE_COLOR)
    }

    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput)
    }

    override fun nextFocusPath(focusNavigationEvent: FocusNavigationEvent): ComponentPath? {
        return null
    }

    override fun onPress(inputWithModifiers: InputWithModifiers) = channelButton.onPress()
}
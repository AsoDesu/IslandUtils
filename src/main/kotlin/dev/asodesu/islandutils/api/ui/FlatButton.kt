package dev.asodesu.islandutils.api.ui

import dev.asodesu.islandutils.api.minecraft
import dev.asodesu.islandutils.api.rect
import dev.asodesu.islandutils.api.rectBorder
import dev.asodesu.islandutils.api.vecRgb
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.gui.navigation.CommonInputs
import net.minecraft.network.chat.Component
import net.minecraft.util.ARGB
import net.minecraft.util.Mth

abstract class FlatButton(message: Component, width: Int = 150, height: Int = 20) : AbstractWidget(0, 0, width, height, message) {
    protected open val COLOR = vecRgb(45, 54, 91)
    protected open val COLOR_FOCUSED = vecRgb(65, 78, 130)
    protected open val PADDING = 8

    var ticksSinceLastHover = 2f

    final override fun renderWidget(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
        if (isHovered) ticksSinceLastHover = 0f
        else ticksSinceLastHover += f

        val t = Mth.clamp(ticksSinceLastHover, 0f, 2f) / 3.0
        val vec = Mth.lerp(t, COLOR_FOCUSED, COLOR)
        val color = ARGB.color(vec)

        guiGraphics.rect(x, y, width, height, color)
        if (isFocused) guiGraphics.rectBorder(x, y, width, height, ARGB.white(1f))

        val font = minecraft.font
        guiGraphics.drawString(font, message, x + PADDING, y + ((this.height - font.lineHeight) / 2) + 1, ARGB.white(1f))
        renderWidget0(guiGraphics, f)
    }

    abstract fun renderWidget0(guiGraphics: GuiGraphics, f: Float)
    abstract fun onPress()

    // appropriated from AbstractButton
    override fun onClick(d: Double, e: Double) = onPress()
    override fun keyPressed(i: Int, j: Int, k: Int): Boolean {
        if (!this.active || !this.visible) {
            return false
        } else if (CommonInputs.selected(i)) {
            this.onPress()
            return true
        } else {
            return false
        }
    }
    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput)
    }
}
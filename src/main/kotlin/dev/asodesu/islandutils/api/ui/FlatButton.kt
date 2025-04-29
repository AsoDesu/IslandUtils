package dev.asodesu.islandutils.api.ui

import dev.asodesu.islandutils.api.minecraft
import dev.asodesu.islandutils.api.rect
import dev.asodesu.islandutils.api.ui.tween.Easing
import dev.asodesu.islandutils.api.ui.tween.Tween
import dev.asodesu.islandutils.api.vecRgb
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.gui.navigation.CommonInputs
import net.minecraft.network.chat.Component
import net.minecraft.util.ARGB
import net.minecraft.world.phys.Vec3
import kotlin.time.Duration.Companion.seconds

abstract class FlatButton(message: Component, width: Int = 150, height: Int = 20) : AbstractWidget(0, 0, width, height, message) {
    protected open val COLORS = mapOf(
        ButtonState.NONE to vecRgb(51, 62, 104),
        ButtonState.HOVERED to vecRgb(65, 78, 130),
        ButtonState.CLICKED to vecRgb(71, 86, 142),
    )
    protected open val PADDING = 8

    var colorTween: Tween<Vec3>? = null
    var state = ButtonState.NONE
    var isClicked = false

    final override fun renderWidget(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
        val newState = buttonState()
        if (state != newState) {
            colorTween = Tween.vec3(targetColor(state), targetColor(newState), 0.2.seconds, Easing.EASE_OUT_EXPO)
            state = newState
        }

        val vec = colorTween?.tick(f) ?: targetColor(state)
        val color = ARGB.color(vec)
        guiGraphics.rect(x, y, width, height, color)
        if (isFocused) guiGraphics.renderOutline(x, y, width, height, ARGB.white(1f))

        val font = minecraft.font
        guiGraphics.drawString(font, message, x + PADDING, y + ((this.height - font.lineHeight) / 2) + 1, ARGB.white(1f))
        renderWidget0(guiGraphics, f)
    }

    fun buttonState() = when {
        isClicked -> ButtonState.CLICKED
        isHovered -> ButtonState.HOVERED
        else -> ButtonState.NONE
    }
    fun targetColor(state: ButtonState) = COLORS[state]!!

    abstract fun renderWidget0(guiGraphics: GuiGraphics, f: Float)
    abstract fun onPress()

    override fun onClick(d: Double, e: Double) {
        onPress()
        isClicked = true
    }
    override fun onRelease(d: Double, e: Double) {
        isClicked = false
    }

    // appropriated from AbstractButton
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

    enum class ButtonState {
        NONE,
        HOVERED,
        CLICKED
    }
}
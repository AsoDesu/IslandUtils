package dev.asodesu.islandutils.api.options.option

import dev.asodesu.islandutils.api.Resources
import dev.asodesu.islandutils.api.minecraft
import dev.asodesu.islandutils.api.options.screen.widgets.FlatButton
import dev.asodesu.islandutils.api.rect
import dev.asodesu.islandutils.api.vecRgb
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.layouts.LayoutElement
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.gui.navigation.CommonInputs
import net.minecraft.client.renderer.RenderType
import net.minecraft.util.ARGB
import net.minecraft.util.Mth

object ToggleOptionRenderer : OptionRenderer<Boolean> {
    override fun render(option: Option<Boolean>): LayoutElement {
        return ToggleButton(option)
    }

    class ToggleButton(val option: Option<Boolean>) : FlatButton(option.component) {
        val TOGGLE_ON = Resources.islandUtils("widget/toggle_on")
        val TOGGLE_OFF = Resources.islandUtils("widget/toggle_off")
        val TOGGLE_WIDTH = 24
        val TOGGLE_HEIGHT = 12

        override fun renderWidget0(guiGraphics: GuiGraphics, f: Float) {
            val sprite = if (option.value) TOGGLE_ON else TOGGLE_OFF

            val toggleX = x + this.width - TOGGLE_WIDTH - PADDING
            val toggleY = y + (this.height - TOGGLE_HEIGHT) / 2
            guiGraphics.blitSprite(RenderType::guiTextured, sprite, toggleX, toggleY, TOGGLE_WIDTH, TOGGLE_HEIGHT)
        }

        override fun onPress() {
            option.value = !option.value
        }
    }
}
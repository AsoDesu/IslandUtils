package dev.asodesu.islandutils.api.ui

import dev.asodesu.islandutils.api.extentions.Resources
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.Component
import net.minecraft.util.ARGB

class ProgressBarWidget(
    val foregroundColor: Int = ARGB.white(1f),
    val backgroundColor: Int = ARGB.color(255, 0, 0, 0),
    x: Int = 0,
    y: Int = 0,
    width: Int = 150,
    height: Int = 20
) : AbstractWidget(x, y, width, height, Component.empty()) {
    private val SPRITE = Resources.islandUtils("widget/rounded_1")
    private var progress = 0.0

    override fun renderWidget(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
        val progressWidth = this.width * progress
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, SPRITE, this.x, this.y, this.width, this.height, backgroundColor)
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, SPRITE, this.x, this.y, progressWidth.toInt(), this.height, foregroundColor)
    }

    fun progress(double: Double) {
        this.progress = double
    }

    override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
    }
}
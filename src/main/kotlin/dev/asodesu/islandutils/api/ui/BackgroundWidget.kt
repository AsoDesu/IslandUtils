package dev.asodesu.islandutils.api.ui

import dev.asodesu.islandutils.api.extentions.Resources
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.Identifier
import net.minecraft.util.ARGB

class BackgroundWidget(
    private val layout: Layout,
    private val spacing: Int,
    private val opacity: Float,
    val sprite: Identifier = ALL
) : Renderable {

    companion object {
        val LEFT = Resources.islandUtils("widget/background/left")
        val ALL = Resources.islandUtils("widget/background/all")
        val RIGHT = Resources.islandUtils("widget/background/right")
    }

    override fun render(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
        val x = layout.x - spacing
        val y = layout.y - spacing
        // add spacing twice to compensate for the spacing we just subtracted
        val width = layout.width + spacing + spacing
        val height = layout.height + spacing + spacing

        guiGraphics.blitRoundedBox(x, y, width, height, sprite, opacity)
    }
}

fun Layout.background(spacing: Int = 0, opacity: Float, sprite: Identifier = BackgroundWidget.ALL) = BackgroundWidget(this, spacing, opacity, sprite)
fun GuiGraphics.blitRoundedBox(x: Int, y: Int, width: Int, height: Int, sprite: Identifier = BackgroundWidget.ALL, opacity: Float = 1f) {
    this.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, x, y, width, height, ARGB.white(opacity))
}
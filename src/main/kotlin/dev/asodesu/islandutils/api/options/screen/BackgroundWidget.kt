package dev.asodesu.islandutils.api.options.screen

import dev.asodesu.islandutils.api.Resources
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Renderable
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.client.renderer.RenderType
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.ARGB

class BackgroundWidget(
    private val layout: Layout,
    private val spacing: Int,
    private val opacity: Float,
    val sprite: ResourceLocation = BackgroundWidget.LEFT
) : Renderable {

    companion object {
        val LEFT = Resources.islandUtils("menu/options_background_left")
        val RIGHT = Resources.islandUtils("menu/options_background_right")
    }

    override fun render(guiGraphics: GuiGraphics, i: Int, j: Int, f: Float) {
        val x = layout.x - spacing
        val y = layout.y - spacing
        // add spacing twice to compensate for the spacing we just subtracted
        val width = layout.width + spacing + spacing
        val height = layout.height + spacing + spacing

        guiGraphics.blitSprite(
            RenderType::guiTextured,
            sprite,
            x,
            y,
            width,
            height,
            ARGB.white(this.opacity)
        )
    }
}

fun Layout.background(spacing: Int = 0, sprite: ResourceLocation, opacity: Float) = BackgroundWidget(this, spacing, opacity, sprite)
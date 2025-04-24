package dev.asodesu.islandutils.api

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.world.phys.Vec3

/**
 * Draws a rectangle at the coordinates with the given width and height
 */
fun GuiGraphics.rect(x: Int, y: Int, width: Int, height: Int, color: Int) {
    this.fill(x, y, x + width, y + height, color)
}

fun GuiGraphics.rectBorder(x: Int, y: Int, width: Int, height: Int, color: Int) {
    this.renderOutline(x, y, width, height, color)
}

fun vecRgb(red: Int, green: Int, blue: Int) = Vec3(red / 255.0, green / 255.0, blue / 255.0)
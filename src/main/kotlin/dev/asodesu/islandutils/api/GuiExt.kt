package dev.asodesu.islandutils.api

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
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

fun MutableComponent.appendLine(component: Component): MutableComponent {
    return this.append(component).newLine()
}

fun MutableComponent.newLine(): MutableComponent {
    return this.append("\n")
}

fun MutableComponent.style(func: Style.() -> Style): Component {
    return this.copy().withStyle { it.func() }
}

fun buildComponent(func: MutableComponent.() -> Unit): Component {
    return Component.empty().also(func)
}
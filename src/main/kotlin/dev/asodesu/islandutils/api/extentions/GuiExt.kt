package dev.asodesu.islandutils.api.extentions

import net.minecraft.client.MouseHandler
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.minecraft.world.phys.Vec3
import org.joml.Matrix3x2fStack

/**
 * Draws a rectangle at the coordinates with the given width and height
 */
fun GuiGraphics.rect(x: Int, y: Int, width: Int, height: Int, color: Int) {
    this.fill(x, y, x + width, y + height, color)
}

inline fun GuiGraphics.scissor(x: Int, y: Int, width: Int, height: Int, apply: () -> Unit) {
    this.enableScissor(x, y, x + width, y + height)
    apply()
    this.disableScissor()
}

inline fun GuiGraphics.pose(apply: Matrix3x2fStack.() -> Unit) {
    pose().pushMatrix()
    apply(pose())
    pose().popMatrix()
}

fun MouseHandler.isInsideBox(x: Int, y: Int, width: Int, height: Int): Boolean {
    val window = minecraft.window
    val mouseX = this.getScaledXPos(window).toInt()
    val mouseY = this.getScaledYPos(window).toInt()
    return isInsideBox(mouseX, mouseY, x, y, width, height)
}

fun isInsideBox(targetX: Int, targetY: Int, x: Int, y: Int, width: Int, height: Int): Boolean {
    val xBound = x + width
    val yBound = y + height
    return targetX >= x && targetY >= y && targetX <= xBound && targetY <= yBound
}

fun vecRgb(red: Int, green: Int, blue: Int) = Vec3(red / 255.0, green / 255.0, blue / 255.0)

fun MutableComponent.appendLine(component: Component): MutableComponent {
    return this.append(component).newLine()
}

fun MutableComponent.newLine(): MutableComponent {
    return this.append("\n")
}

inline fun MutableComponent.copyAndStyle(crossinline func: Style.() -> Style): Component {
    return this.copy().withStyle { it.func() }
}
inline fun MutableComponent.style(crossinline func: Style.() -> Style): Component {
    return this.withStyle { it.func() }
}

inline fun buildComponent(func: MutableComponent.() -> Unit): Component {
    return Component.empty().also(func)
}
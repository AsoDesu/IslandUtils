package dev.asodesu.islandutils.api.options.screen.tab

import java.util.function.Consumer
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.client.gui.layouts.LayoutElement
import net.minecraft.client.gui.navigation.ScreenRectangle

interface ConfigScreenTab : LayoutElement {

    val layout: Layout

    fun setMinWidth(width: Int)

    fun init()
    fun close()

    // why can't kotlin delegates do this in interfaces >:(
    override fun setX(i: Int) { layout.x = i }
    override fun setY(i: Int) { layout.y = i }
    override fun getX() = layout.x
    override fun getY() = layout.y
    override fun getWidth() = layout.width
    override fun getHeight() = layout.height
    override fun visitWidgets(consumer: Consumer<AbstractWidget>) = layout.visitWidgets(consumer)
    override fun getRectangle(): ScreenRectangle = layout.rectangle
    override fun setPosition(i: Int, j: Int) = layout.setPosition(i, j)
}
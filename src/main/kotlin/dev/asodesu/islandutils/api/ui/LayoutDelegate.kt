package dev.asodesu.islandutils.api.ui

import java.util.function.Consumer
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.client.gui.layouts.LayoutElement

abstract class LayoutDelegate : Layout {
    abstract val layout: Layout

    override fun setX(i: Int) { layout.x = i }
    override fun setY(i: Int) { layout.y = i }
    override fun getX() = layout.x
    override fun getY() = layout.y
    override fun getWidth() = layout.width
    override fun getHeight() = layout.height
    override fun arrangeElements() = layout.arrangeElements()
    override fun getRectangle() = layout.rectangle
    override fun setPosition(i: Int, j: Int) = layout.setPosition(i, j)
    override fun visitWidgets(consumer: Consumer<AbstractWidget>) = layout.visitWidgets(consumer)
    override fun visitChildren(consumer: Consumer<LayoutElement>) = layout.visitChildren(consumer)
}
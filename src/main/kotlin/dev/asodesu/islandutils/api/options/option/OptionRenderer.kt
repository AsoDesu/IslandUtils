package dev.asodesu.islandutils.api.options.option

import net.minecraft.client.gui.layouts.LayoutElement

interface OptionRenderer<T> {
    fun render(option: Option<T>): LayoutElement
}
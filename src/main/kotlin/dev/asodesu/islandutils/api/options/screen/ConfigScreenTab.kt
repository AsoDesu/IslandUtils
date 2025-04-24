package dev.asodesu.islandutils.api.options.screen

import net.minecraft.client.gui.layouts.Layout

interface ConfigScreenTab {

    val layout: Layout
    fun init()
    fun close()

}
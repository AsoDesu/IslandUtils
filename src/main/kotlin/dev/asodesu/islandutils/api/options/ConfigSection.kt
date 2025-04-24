package dev.asodesu.islandutils.api.options

import dev.asodesu.islandutils.api.minecraft
import dev.asodesu.islandutils.api.options.screen.ConfigScreenTab
import dev.asodesu.islandutils.api.options.screen.SectionScreenTab
import net.minecraft.client.gui.components.StringWidget
import net.minecraft.client.gui.layouts.Layout
import net.minecraft.client.gui.layouts.LinearLayout
import net.minecraft.network.chat.Component

abstract class ConfigSection(name: String) : ConfigGroup(name) {
    fun render() = SectionScreenTab(this)
}
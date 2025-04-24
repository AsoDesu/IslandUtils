package dev.asodesu.islandutils.api.options

import dev.asodesu.islandutils.api.options.screen.tab.SectionScreenTab

abstract class ConfigSection(name: String) : ConfigGroup(name) {
    override fun render() = SectionScreenTab(this)
}
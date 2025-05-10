package dev.asodesu.islandutils.options

import dev.asodesu.islandutils.api.options.ConfigGroup
import dev.asodesu.islandutils.api.options.ConfigSection

object MiscOptions : ConfigSection("section.misc") {

    val scavengingTotals = toggle("chest_scavenging_totals", desc = true, def = true)
    val remnantHighlight = toggle("chest_remnant_highlight", desc = true, def = true)
    val fishingUpgrades = toggle("chest_fishing_upgrade", desc = true, def = true)

    object Debug : ConfigGroup("section.misc.debug") {
        val debugMode = toggle(name = "debug_mode", desc = true, def = false)
    }

    init {
        group(Debug)
    }

}
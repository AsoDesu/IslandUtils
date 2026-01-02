package dev.asodesu.islandutils.options

import dev.asodesu.islandutils.api.options.ConfigSection

object CosmeticOptions : ConfigSection("section.cosmetics") {
    val enabled = toggle("cosmetics_enabled", def = true, desc = false)
    val showOnHover = toggle("cosmetics_show_on_hover", def = true, desc = true)
    val showInAllMenus = toggle("cosmetics_show_in_all_menus", def = false, desc = true)
    val showInGames = toggle("cosmetics_show_in_games", def = true, desc = true)
}
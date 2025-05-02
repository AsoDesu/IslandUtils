package dev.asodesu.islandutils.options

import dev.asodesu.islandutils.api.options.ConfigSection

object NotificationOptions : ConfigSection("section.notifs") {
    val showInServerMenu = toggle("notifs_server_menu", def = true, desc = true)
    val craftingNotifications = toggle("notifs_crafting", def = true, desc = true)
}
package dev.asodesu.islandutils.options

import dev.asodesu.islandutils.api.options.ConfigGroup
import dev.asodesu.islandutils.api.options.ConfigSection

object NotificationOptions : ConfigSection("section.notifs") {
    val showInServerMenu = toggle("notifs_server_menu", def = true, desc = true)
    val craftingNotifications = toggle("notifs_crafting", def = true, desc = true)

    object FriendsInGame : ConfigGroup("section.notifs.friendnotifs") {
        val inGame = toggle(name = "friendnotifs_in_game", desc = true, def = true)
        val inLobby = toggle(name = "friendnotifs_in_lobby", desc = true, def = true)
    }

    init {
        group(FriendsInGame)
    }
}
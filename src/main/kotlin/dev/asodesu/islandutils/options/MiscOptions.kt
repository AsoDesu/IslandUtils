package dev.asodesu.islandutils.options

import dev.asodesu.islandutils.api.options.ConfigGroup
import dev.asodesu.islandutils.api.options.ConfigSection

object MiscOptions : ConfigSection("section.misc") {

    val testOption = toggle(name = "test", desc = true, def = true)

    object FriendsInGame : ConfigGroup("section.misc.friendnotifs") {
        val inGame = toggle(name = "friendnotifs_in_game", desc = true, def = true)
        val inLobby = toggle(name = "friendnotifs_in_lobby", desc = true, def = true)
    }

    init {
        group(FriendsInGame)
    }

}
package dev.asodesu.islandutils.api.discord.container

import dev.asodesu.islandutils.api.discord.DiscordManager
import dev.asodesu.islandutils.api.events.EventConsumer

object EmptyDiscordContainer : DiscordContainer {
    override fun eventChildren() = emptyList<EventConsumer>()
    override fun update() {
        DiscordManager.clearActivity()
    }

    override fun toString() = "EmptyDiscordContainer"
}
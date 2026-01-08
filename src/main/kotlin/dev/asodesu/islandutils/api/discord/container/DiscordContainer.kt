package dev.asodesu.islandutils.api.discord.container

import dev.asodesu.islandutils.api.discord.Discord
import dev.asodesu.islandutils.api.discord.DiscordActivity
import dev.asodesu.islandutils.api.discord.layers.images
import dev.asodesu.islandutils.api.discord.layers.timeElapsed
import dev.asodesu.islandutils.api.events.EventConsumer
import dev.asodesu.islandutils.api.server.ServerSessionHandler
import kotlin.time.Duration.Companion.hours

interface DiscordContainer {
    fun update()
    fun eventChildren(): List<EventConsumer>

    companion object {
        val empty = EmptyDiscordContainer
        val default = Discord {
            images("hub", "mcci")
            timeElapsed(ServerSessionHandler.joinTime)
        }
        val bigRat = DiscordActivity {
            assets {
                largeImage = "bigrat"
                largeText = "BIG RAT"
            }
            state = "BIG RAT BIG RAT BIG RAT"
            details = "BIG RAT BIG RAT BIG RAT"
            timestamps {
                start = System.currentTimeMillis() - 24.hours.inWholeMilliseconds
            }
        }
    }
}
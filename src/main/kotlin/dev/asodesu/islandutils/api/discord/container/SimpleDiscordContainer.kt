package dev.asodesu.islandutils.api.discord.container

import dev.asodesu.islandutils.api.discord.DiscordManager
import dev.asodesu.islandutils.api.events.EventConsumer
import io.github.vyfor.kpresence.rpc.ActivityBuilder

class SimpleDiscordContainer(val func: ActivityBuilder.() -> Unit) : DiscordContainer {
    override fun eventChildren() = emptyList<EventConsumer>()

    override fun update() {
        val activity = ActivityBuilder()
        activity.func()
        DiscordManager.updateActivity(activity.build())
    }

    override fun toString() = "SimpleDiscordContainer"
}
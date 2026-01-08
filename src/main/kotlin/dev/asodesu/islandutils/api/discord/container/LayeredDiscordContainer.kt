package dev.asodesu.islandutils.api.discord.container

import dev.asodesu.islandutils.api.discord.ActivityLayer
import dev.asodesu.islandutils.api.discord.DiscordManager
import io.github.vyfor.kpresence.rpc.ActivityBuilder

class LayeredDiscordContainer(val layers: List<ActivityLayer> = emptyList()) : DiscordContainer {
    override fun eventChildren() = layers

    override fun update() {
        val activity = ActivityBuilder()
        layers.forEach { it.update(activity) }
        DiscordManager.updateActivity(activity.build())
    }

    override fun toString() = "LayeredDiscordContainer(layers=[${layers.joinToString(",") { it::class.simpleName.toString() }}])"
}
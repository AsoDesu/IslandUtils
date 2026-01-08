package dev.asodesu.islandutils.api.discord

import dev.asodesu.islandutils.api.discord.container.DiscordContainer
import dev.asodesu.islandutils.api.discord.container.LayeredDiscordContainer
import dev.asodesu.islandutils.api.discord.container.SimpleDiscordContainer
import io.github.vyfor.kpresence.rpc.ActivityBuilder

class ActivityContainerBuilder(private val layers: MutableList<ActivityLayer> = mutableListOf()) {
    fun <T : ActivityLayer> layer(layer: T): T {
        layers += layer
        return layer
    }

    fun build() = LayeredDiscordContainer(layers)
}

typealias ActivityContainerBuilderFunction = ActivityContainerBuilder.() -> Unit
fun ActivityContainerBuilderFunction.build(): DiscordContainer {
    return ActivityContainerBuilder().also { this.invoke(it) }.build()
}
fun Discord(func: ActivityContainerBuilderFunction): ActivityContainerBuilderFunction {
    return func
}
fun DiscordActivity(func: ActivityBuilder.() -> Unit): SimpleDiscordContainer {
    return SimpleDiscordContainer(func)
}
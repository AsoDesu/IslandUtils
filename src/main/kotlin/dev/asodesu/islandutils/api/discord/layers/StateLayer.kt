package dev.asodesu.islandutils.api.discord.layers

import dev.asodesu.islandutils.api.discord.ActivityContainerBuilder
import dev.asodesu.islandutils.api.discord.ActivityLayer
import io.github.vyfor.kpresence.rpc.ActivityBuilder

fun ActivityContainerBuilder.state(text: String) = state { text }
fun ActivityContainerBuilder.state(stateConsumer: () -> String?) = layer(StateLayer(stateConsumer))

class StateLayer(val stateConsumer: () -> String?) : ActivityLayer {
    override fun update(activity: ActivityBuilder) {
        activity.state = stateConsumer()
    }
}
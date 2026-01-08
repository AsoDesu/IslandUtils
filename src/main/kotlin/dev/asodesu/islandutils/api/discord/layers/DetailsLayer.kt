package dev.asodesu.islandutils.api.discord.layers

import dev.asodesu.islandutils.api.discord.ActivityContainerBuilder
import dev.asodesu.islandutils.api.discord.ActivityLayer
import io.github.vyfor.kpresence.rpc.ActivityBuilder

fun ActivityContainerBuilder.details(text: String) = layer(DetailsLayer(text))
fun ActivityContainerBuilder.playingDetails(text: String) = layer(DetailsLayer("Playing $text"))


class DetailsLayer(val details: String) : ActivityLayer {
    override fun update(activity: ActivityBuilder) {
        activity.details = details
    }
}
package dev.asodesu.islandutils.api.discord.layers

import dev.asodesu.islandutils.api.discord.ActivityContainerBuilder
import dev.asodesu.islandutils.api.discord.ActivityLayer
import io.github.vyfor.kpresence.rpc.ActivityBuilder
import io.github.vyfor.kpresence.rpc.ActivityTimestamps

fun ActivityContainerBuilder.timeElapsed() = layer(TimeElapsedLayer())
fun ActivityContainerBuilder.timeElapsed(timeStart: Long) = layer(TimeElapsedLayer(timeStart))

class TimeElapsedLayer(val timeStart: Long = System.currentTimeMillis()) : ActivityLayer {
    override fun update(activity: ActivityBuilder) {
        activity.timestamps = ActivityTimestamps(timeStart)
    }
}
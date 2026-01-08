package dev.asodesu.islandutils.api.discord

import dev.asodesu.islandutils.api.events.EventConsumer
import io.github.vyfor.kpresence.rpc.ActivityBuilder

interface ActivityLayer : EventConsumer {
    fun update(activity: ActivityBuilder)
}
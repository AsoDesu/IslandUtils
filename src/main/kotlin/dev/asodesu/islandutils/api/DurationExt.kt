package dev.asodesu.islandutils.api

import kotlin.time.Duration

val Duration.ticks: Int
    get() = this.inWholeMilliseconds.toInt() / 50
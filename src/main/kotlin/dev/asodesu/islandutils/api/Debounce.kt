package dev.asodesu.islandutils.api

import kotlin.time.Duration

class Debounce(cooldown: Duration) {
    private val duration = cooldown.inWholeNanoseconds
    private var lastTrigger = 0L

    fun consume(): Boolean {
        if (lastTrigger() < duration) return false
        lastTrigger = System.nanoTime()
        return true
    }

    fun lastTrigger() = System.nanoTime() - lastTrigger
}
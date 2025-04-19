package dev.asodesu.islandutils.api

import dev.asodesu.islandutils.api.Scheduler.Task
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.Minecraft
import kotlin.time.Duration

/**
 * A simple tick scheduler
 */
object Scheduler {
    private val tasks = mutableListOf<Task>()
    fun init() {
        ClientTickEvents.END_CLIENT_TICK.register {
            this.tick(it)
        }
    }

    private fun tick(client: Minecraft) {
        if (tasks.isEmpty()) return

        tasks.removeIf { it.tick(client) }
    }

    /**
     * Schedule a new task to execute in the specified duration
     *
     * @param duration The amount of time to wait
     * @param callback The task to execute
     */
    fun runAfter(duration: Duration, callback: (Minecraft) -> Unit): Task {
        return Task(duration, callback)
            .also { tasks += it }
    }

    class Task(duration: Duration, private val callback: (Minecraft) -> Unit) {
        private var remove = false
        private var ticks = duration.ticks

        fun tick(client: Minecraft): Boolean {
            if (remove) return true

            if (--ticks > 0) return false
            callback(client)
            return true
        }

        fun cancel() {
            remove = true
        }
    }
}
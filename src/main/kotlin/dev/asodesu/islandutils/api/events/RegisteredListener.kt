package dev.asodesu.islandutils.api.events

class RegisteredListener<T : Any>(private val event: MutableListEvent<T>, val handle: T) {
    fun unregister() = event.unregister(this)
}
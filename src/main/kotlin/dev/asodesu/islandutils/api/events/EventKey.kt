package dev.asodesu.islandutils.api.events

abstract class EventKey {
    private val listeners = mutableListOf<RegisteredListener<*>>()

    fun add(listener: RegisteredListener<*>) = listeners.add(listener)

    protected fun <T : Any> MutableListEvent<T>.register(listener: T): RegisteredListener<T> {
        return this.addListener(listener).also {
            this@EventKey.listeners += it
        }
    }

    fun unregisterEvents() {
        listeners.forEach { it.unregister() }
        listeners.clear()
    }
}
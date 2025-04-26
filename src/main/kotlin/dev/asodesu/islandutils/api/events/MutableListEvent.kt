package dev.asodesu.islandutils.api.events

import kotlin.properties.Delegates

/**
 * An event which can be registered and unregistered, backed by a mutable list
 *
 * @param invokerFactory The invoker for this event
 */
class MutableListEvent<T : Any>(private val invokerFactory: (List<T>) -> T) {
    private val handlers = mutableListOf<RegisteredListener<T>>()
    private var invoker: T by Delegates.notNull()

    init {
        rebuildInvoker()
    }

    fun register(key: EventKey? = null, listener: T): RegisteredListener<T> {
        return addListener(listener)
            .also { key?.add(it) }
    }

    fun addListener(listener: T): RegisteredListener<T> {
        return RegisteredListener(this, listener).also {
            handlers += it
            rebuildInvoker()
        }
    }

    /**
     * Removes this listener from this event.
     *
     * @param listener The listener to remove
     * @return true if any listeners were removed
     */
    fun unregister(listener: RegisteredListener<*>): Boolean {
        return handlers.remove(listener)
            .also { rebuildInvoker() }
    }

    private fun rebuildInvoker() {
        this.invoker = invokerFactory(handlers.map { it.handle })
    }

    fun invoker() = invoker

}
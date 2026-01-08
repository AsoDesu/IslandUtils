package dev.asodesu.islandutils.api.options

import dev.asodesu.islandutils.api.options.option.Option

fun Option<Boolean>.onEnable(func: () -> Unit) = apply {
    this.onChange { last, new ->
        if (last != new && new) func()
    }
}

fun Option<Boolean>.onDisabled(func: () -> Unit) = apply {
    this.onChange { last, new ->
        if (last != new && !new) func()
    }
}

fun Option<Boolean>.onChange(func: () -> Unit) = apply {
    this.onChange { last, new -> func() }
}
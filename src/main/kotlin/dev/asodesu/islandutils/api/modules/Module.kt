package dev.asodesu.islandutils.api.modules

import dev.asodesu.islandutils.api.debug
import org.slf4j.LoggerFactory

/**
 * A module
 * TODO: good documentation
 */
abstract class Module(name: String) {
    protected val logger = LoggerFactory.getLogger("IU-${name}")
    abstract fun init()
}
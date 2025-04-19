package dev.asodesu.islandutils.api.modules

import org.slf4j.LoggerFactory
import kotlin.math.log

/**
 * A module
 * TODO: good documentation
 */
abstract class Module(name: String) {
    protected val logger = LoggerFactory.getLogger("IU-${name}")
    abstract fun init()

    fun debug(str: String) {
        logger.info(str)
        // TODO: Chat Log
    }
}
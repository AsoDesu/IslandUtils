package dev.asodesu.islandutils.api.modules

import dev.asodesu.islandutils.Modules
import java.lang.RuntimeException
import org.slf4j.LoggerFactory
import kotlin.reflect.full.memberProperties

object ModuleManager {
    private val logger = LoggerFactory.getLogger("IU-ModuleManager")
    private val modules = buildList {
        // setup modules object
        val instance = Modules::class.objectInstance ?: throw RuntimeException("Modules is not an object.")
        Modules::class.memberProperties.forEach {
            val obj = it.get(instance)
            if (obj is Module) add(obj)
        }

        addAll(Modules.objects)
    }

    fun init() {
        logger.info("Initialising modules...")
        var successes = 0
        modules.forEach {
            try {
                it.init()
                successes++
            } catch (e: Exception) {
                logger.error("Failed to initialise module '${it::class.qualifiedName}'", e)
            }
        }
        logger.info("Initialised $successes modules!")
    }
}
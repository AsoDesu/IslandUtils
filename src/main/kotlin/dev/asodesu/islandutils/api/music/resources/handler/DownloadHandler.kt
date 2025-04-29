package dev.asodesu.islandutils.api.music.resources.handler

import net.minecraft.network.chat.Component
import org.slf4j.LoggerFactory

abstract class DownloadHandler {
    protected val logger = LoggerFactory.getLogger("IU-Download")
    protected var thread: Thread? = null
    var job: DownloadJob? = null

    open var progress: Double = 0.0
    open var state: Component = Component.empty()
    open var finished: Boolean = false

    abstract fun run()

    abstract fun progress(asset: String, progress: Double)
    fun fail(asset: String, e: Exception) {
        logger.error("Failed to download '$asset'", e)
    }
    open fun done(asset: String) {
        logger.info("Downloaded asset '$asset'!")
    }

    open fun cancel() {
        thread?.interrupt()
        finally()
    }

    fun finally() {
        finished = true
        job?.finish()
    }
}
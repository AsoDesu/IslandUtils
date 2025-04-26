package dev.asodesu.islandutils.api.music.resources.handler

import dev.asodesu.islandutils.api.minecraft
import dev.asodesu.islandutils.api.music.resources.DownloadScreen
import dev.asodesu.islandutils.api.music.resources.RemoteResources
import dev.asodesu.islandutils.api.options.screen.ConfigScreen
import net.minecraft.network.chat.Component
import org.slf4j.LoggerFactory

class MultiDownloadHandler(assets: List<String>) : DownloadProgressListener {
    private val logger = LoggerFactory.getLogger("IU-Download")
    private var downloadScreen: DownloadScreen? = null
    private val downloadQueue = mutableListOf<String>()
    private var i = 0
    private var thread: Thread? = null

    override var progress: Double = 0.0
    override var state: Component = Component.empty()

    init {
        assets.forEach {
            RemoteResources.use(it)
            if (!RemoteResources.downloaded(it)) downloadQueue += it
        }
        if (downloadQueue.isNotEmpty()) {
            if (minecraft.screen is ConfigScreen) {
                downloadScreen = DownloadScreen(this, minecraft.screen)
                minecraft.setScreen(downloadScreen)
            }
            thread = Thread(::run).also { it.start() }
        }
    }

    fun run() {
        for (asset in downloadQueue) {
            i++
            try {
                RemoteResources.download(asset, this)
            } catch (e: Exception) {
                if (e is InterruptedException) {
                    logger.error("Download of $asset interrupted, cancelling.")
                    RemoteResources.delete(asset)
                    break
                }
            }
        }
        minecraft.submit { downloadScreen?.close() }
    }

    override fun progress(asset: String, progress: Double) {
        val startingProgress = i / downloadQueue.size.toDouble()
        val scaledProgress = progress / downloadQueue.size
        this.progress = startingProgress + scaledProgress
        this.state = Component.literal("($i/${downloadQueue.size}) $asset")
    }

    override fun fail(asset: String, e: Exception) {
        logger.error("Failed to download '$asset'", e)
    }

    override fun done(asset: String) {
        logger.info("Downloaded asset '$asset'!")
    }

    override fun cancel() {
        thread?.interrupt()
    }
}
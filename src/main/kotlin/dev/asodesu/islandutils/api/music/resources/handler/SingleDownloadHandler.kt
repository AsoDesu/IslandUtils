package dev.asodesu.islandutils.api.music.resources.handler

import dev.asodesu.islandutils.api.minecraft
import dev.asodesu.islandutils.api.music.resources.DownloadScreen
import dev.asodesu.islandutils.api.music.resources.RemoteResources
import dev.asodesu.islandutils.api.options.screen.ConfigScreen
import net.minecraft.network.chat.Component
import org.slf4j.LoggerFactory

class SingleDownloadHandler(val asset: String) : DownloadProgressListener {
    private val logger = LoggerFactory.getLogger("IU-Download")

    private var downloadScreen: DownloadScreen? = null
    private var thread: Thread? = null

    override var progress: Double = 0.0
    override var state: Component = Component.literal("(1/1) $asset")

    init {
        RemoteResources.use(asset)
        if (!RemoteResources.downloaded(asset)) {
            if (minecraft.screen is ConfigScreen) {
                downloadScreen = DownloadScreen(this, minecraft.screen)
                minecraft.setScreen(downloadScreen)
            }
            thread = Thread(::run).also { it.start() }
        }
    }

    fun run() {
        try {
            RemoteResources.download(asset, this)
        } catch (e: Exception) {
        }
        minecraft.submit { downloadScreen?.close() }
    }

    override fun progress(asset: String, progress: Double) {
        this.progress = progress
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
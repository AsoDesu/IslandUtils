package dev.asodesu.islandutils.api.music.resources.handler

import dev.asodesu.islandutils.api.music.resources.RemoteResources
import net.minecraft.network.chat.Component

class MultiDownloadHandler(assets: Collection<String>) : DownloadHandler() {
    private val downloadQueue = mutableListOf<String>()
    private var i = 0

    init {
        assets.forEach {
            RemoteResources.use(it)
            if (!RemoteResources.downloaded(it)) downloadQueue += it
        }
        if (downloadQueue.isNotEmpty()) {
            thread = Thread(::run).also { it.start() }
        } else {
            finally()
        }
    }

    override fun run() {
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
        finally()
    }

    override fun progress(asset: String, progress: Double) {
        val startingProgress = i / downloadQueue.size.toDouble()
        val scaledProgress = progress / downloadQueue.size
        this.progress = startingProgress + scaledProgress
        this.state = Component.literal("($i/${downloadQueue.size}) $asset")
    }
}
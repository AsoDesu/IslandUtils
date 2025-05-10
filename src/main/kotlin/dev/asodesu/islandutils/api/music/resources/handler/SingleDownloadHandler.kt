package dev.asodesu.islandutils.api.music.resources.handler

import dev.asodesu.islandutils.api.music.resources.RemoteResources
import net.minecraft.network.chat.Component

class SingleDownloadHandler(val asset: String) : DownloadHandler() {
    override var progress: Double = 0.0
    override var state: Component = Component.literal("(1/1) $asset")

    init {
        RemoteResources.use(asset)
        if (!RemoteResources.downloaded(asset)) {
            thread = Thread(::run).also { it.start() }
        } else {
            finally()
        }
    }

    override fun run() {
        try {
            RemoteResources.download(asset, this)
        } catch (e: Exception) {
        }
        this.finally()
    }

    override fun progress(asset: String, progress: Double) {
        this.progress = progress
    }
}
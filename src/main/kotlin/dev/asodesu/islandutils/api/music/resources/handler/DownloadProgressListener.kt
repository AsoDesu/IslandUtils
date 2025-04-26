package dev.asodesu.islandutils.api.music.resources.handler

import net.minecraft.network.chat.Component

interface DownloadProgressListener {
    var progress: Double
    var state: Component

    fun progress(asset: String, progress: Double)
    fun fail(asset: String, e: Exception)
    fun done(asset: String)

    fun cancel()
}
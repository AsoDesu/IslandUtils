package dev.asodesu.islandutils.api.music.resources.handler

class DownloadJob(val factory: () -> DownloadHandler) {
    var currentDownload: DownloadHandler? = null
        private set

    fun start() {
        if (currentDownload != null) return
        factory().also { handler ->
            if (handler.finished) return
            currentDownload = handler
            handler.job = this
        }
    }

    fun restart() {
        currentDownload?.cancel()
        currentDownload = null
        start()
    }

    fun finish() {
        currentDownload = null
    }

    companion object {
        fun single(asset: String) = DownloadJob { SingleDownloadHandler(asset) }
        fun multi(assets: Collection<String>) = DownloadJob { MultiDownloadHandler(assets) }
        fun multi(vararg assets: String) = DownloadJob { MultiDownloadHandler(assets.toList()) }
    }
}
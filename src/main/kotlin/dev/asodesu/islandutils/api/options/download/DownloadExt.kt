package dev.asodesu.islandutils.api.options.download

import dev.asodesu.islandutils.api.music.resources.handler.DownloadJob
import dev.asodesu.islandutils.api.options.onEnable
import dev.asodesu.islandutils.api.options.option.Option

fun <T> Option<T>.withDownloadJob(job: DownloadJob, startTrigger: (() -> Unit) -> Any) = apply {
    startTrigger { job.start() }
    this.withRenderer(DownloaderOptionRenderer(this.renderer(), job))
}

fun Option<Boolean>.withDownloadJob(job: DownloadJob) = withDownloadJob(job, this::onEnable)
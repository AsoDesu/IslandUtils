package dev.asodesu.islandutils.api.music.resources

import dev.asodesu.islandutils.api.Resources
import dev.asodesu.islandutils.api.configDir
import dev.asodesu.islandutils.api.music.resources.handler.DownloadHandler
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.FileOutputStream
import java.io.IOException
import java.net.URI
import net.minecraft.FileUtil
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists

object RemoteResources {
    private val assetsBaseUrl = "https://raw.githubusercontent.com/AsoDesu/islandutils-assets/2.0.0/assets/"
    private val assetsFolder = configDir.resolve("islandutils_assets")

    fun downloaded(asset: String) = file(asset).exists()

    fun use(asset: String) {
        SoundInjector.inject(key(asset), file(asset))
    }

    fun delete(asset: String) {
        file(asset).deleteIfExists()
    }

    fun download(asset: String, progressListener: DownloadHandler? = null) {
        val outputFile = file(asset)
        try {
            FileUtil.createDirectoriesSafe(outputFile.parent)
        } catch (e: Exception) {
            throw IOException("Failed to create assets directory", e)
        }

        var input: BufferedInputStream? = null
        var output: BufferedOutputStream? = null
        try {
            val url = URI("$assetsBaseUrl$asset.ogg").toURL()
            val http = url.openConnection()

            val totalLength = http.contentLengthLong
            var bytesDownloaded = 0L

            input = BufferedInputStream(http.getInputStream())
            output = BufferedOutputStream(FileOutputStream(outputFile.toFile()), 1024)
            val data = ByteArray(1024)

            var x: Int
            while (input.read(data, 0, 1024).also { x = it } >= 0) {
                bytesDownloaded += x
                progressListener?.progress(asset, bytesDownloaded / totalLength.toDouble())
                output.write(data, 0, x)

                if (Thread.interrupted()) throw InterruptedException()
            }
            progressListener?.done(asset)
            input.close()
            output.close()
        } catch (e: Exception){
            progressListener?.fail(asset, e)
            input?.close()
            output?.close()
            throw e
        }
    }

    fun key(asset: String) = Resources.islandUtils(asset.replace("/", "."))
    fun file(asset: String) = assetsFolder.resolve("$asset.ogg")

}
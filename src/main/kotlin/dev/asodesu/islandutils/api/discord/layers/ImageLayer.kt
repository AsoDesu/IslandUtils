package dev.asodesu.islandutils.api.discord.layers

import dev.asodesu.islandutils.IslandUtils
import dev.asodesu.islandutils.api.discord.ActivityContainerBuilder
import dev.asodesu.islandutils.api.discord.ActivityLayer
import io.github.vyfor.kpresence.rpc.ActivityBuilder

fun ActivityContainerBuilder.images(largeImage: String) = layer(ImageLayer(largeImage))
fun ActivityContainerBuilder.images(largeImage: String, smallImage: String) = layer(ImageLayer(largeImage, smallImage))

class ImageLayer(val largeAsset: String, val smallAsset: String = "mcci") : ActivityLayer {
    override fun update(activity: ActivityBuilder) {
        activity.assets {
            largeImage = largeAsset
            largeText = "MCC Island"

            smallImage = smallAsset
            smallText = "IslandUtils - ${IslandUtils.modVersion}"
        }
    }
}
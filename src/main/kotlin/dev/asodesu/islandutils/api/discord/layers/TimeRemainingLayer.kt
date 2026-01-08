package dev.asodesu.islandutils.api.discord.layers

import dev.asodesu.islandutils.api.discord.ActivityContainerBuilder
import dev.asodesu.islandutils.api.discord.ActivityLayer
import dev.asodesu.islandutils.api.discord.DiscordManager
import dev.asodesu.islandutils.api.extentions.debug
import io.github.vyfor.kpresence.rpc.ActivityBuilder
import io.github.vyfor.kpresence.rpc.ActivityTimestamps
import java.util.*
import net.minecraft.network.chat.Component

fun ActivityContainerBuilder.timeRemaining() = layer(TimeRemainingLayer())

class TimeRemainingLayer() : ActivityLayer {
    private val timerRegex = Regex("(\\d+):(\\d+)")
    var startingTime = System.currentTimeMillis()
    var endingTime = System.currentTimeMillis()

    override fun update(activity: ActivityBuilder) {
        activity.timestamps = ActivityTimestamps(startingTime, endingTime)
    }

    override fun onBossbarContents(uuid: UUID, contents: Component) {
        val string = contents.string
        debug("Got bossbar contents with a length of ${string.length}")
        // check for if this bossbar actually contains a timer
        if (!string.contains(":")) return

        val match = timerRegex.find(string) ?: return
        val mins = match.groupValues.getOrNull(1)?.toIntOrNull() ?: return
        val secs = match.groupValues.getOrNull(2)?.toIntOrNull() ?: return

        // this is how time works!!
        val secondsLeft = (mins * 60L) + secs + 1
        endingTime = System.currentTimeMillis() + (secondsLeft * 1000)
        DiscordManager.update()
    }
}
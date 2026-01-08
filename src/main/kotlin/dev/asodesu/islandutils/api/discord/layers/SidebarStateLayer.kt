package dev.asodesu.islandutils.api.discord.layers

import dev.asodesu.islandutils.api.discord.ActivityContainerBuilder
import dev.asodesu.islandutils.api.discord.ActivityLayer
import dev.asodesu.islandutils.api.discord.DiscordManager
import dev.asodesu.islandutils.api.extentions.withValue
import io.github.vyfor.kpresence.rpc.ActivityBuilder
import net.minecraft.network.chat.Component

fun ActivityContainerBuilder.sidebarStateLayer(pattern: String, prefix: String = "") = layer(SidebarStateLayer(Regex(pattern), prefix))
fun ActivityContainerBuilder.roundState() = sidebarStateLayer("ROUNDS \\[(\\d*/\\d*)]", "Round: ")
fun ActivityContainerBuilder.playersRemainingState() = sidebarStateLayer("PLAYERS REMAINING: (\\d*/\\d*)", "Remaining: ")

class SidebarStateLayer(private val regex: Regex, val prefix: String = "") : ActivityLayer {
    var value: String? = null

    override fun update(activity: ActivityBuilder) {
        activity.state = value
    }

    override fun onSidebarLine(component: Component) {
        regex.withValue(component) {
            value = prefix + it
            DiscordManager.update()
        }
    }
}
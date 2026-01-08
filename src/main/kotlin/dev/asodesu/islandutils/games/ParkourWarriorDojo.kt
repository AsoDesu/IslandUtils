package dev.asodesu.islandutils.games

import com.noxcrew.noxesium.core.mcc.ClientboundMccServerPacket
import dev.asodesu.islandutils.api.discord.Discord
import dev.asodesu.islandutils.api.discord.DiscordManager
import dev.asodesu.islandutils.api.discord.layers.images
import dev.asodesu.islandutils.api.discord.layers.playingDetails
import dev.asodesu.islandutils.api.discord.layers.state
import dev.asodesu.islandutils.api.discord.layers.timeElapsed
import dev.asodesu.islandutils.api.extentions.withValue
import dev.asodesu.islandutils.api.game.Game
import dev.asodesu.islandutils.api.game.context.GameContext
import net.minecraft.network.chat.Component

class ParkourWarriorDojo(val courseType: String, types: List<String>) : Game("parkour_warrior_dojo", types) {
    override val hasTeamChat = false
    override val discord = Discord {
        images("parkour_warrior_dojo")
        playingDetails("Parkour Warrior: Dojo")
        state { course }
        timeElapsed()
    }

    private val courseRegex = Regex("COURSE: (.+)")
    var course: String? = null

    override fun onSidebarLine(component: Component) {
        courseRegex.withValue(component) {
            course = it
            DiscordManager.update()
        }
    }

    override fun toString() = "ParkourWarriorDojo(course=$courseType)"

    companion object : GameContext {
        override val id: String = "parkour_warrior_dojo"
        override fun check(packet: ClientboundMccServerPacket): Boolean {
            return packet.server == "dojo" && packet.checkTypes("parkour_warrior", "dojo")
        }

        override fun create(packet: ClientboundMccServerPacket): Game {
            val course = packet.types.firstOrNull { it.startsWith("main-") || it == "daily" }
                ?: throw IllegalStateException("Couldn't find course from server types")
            return ParkourWarriorDojo(course, packet.types)
        }
    }
}
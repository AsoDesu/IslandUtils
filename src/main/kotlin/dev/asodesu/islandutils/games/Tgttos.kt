package dev.asodesu.islandutils.games

import com.noxcrew.noxesium.core.mcc.ClientboundMccServerPacket
import dev.asodesu.islandutils.api.extentions.withValue
import dev.asodesu.islandutils.api.game.Game
import dev.asodesu.islandutils.api.game.context.SimpleGameContext
import net.minecraft.network.chat.Component

class Tgttos(types: List<String>) : Game("tgttos", types) {
    override val hasTeamChat = false

    val modifierLineRegex = Regex("MODIFIER: ([\\w ]+)")
    var modifier: String = ""

    override fun onSidebarLine(component: Component) {
        modifierLineRegex.withValue(component) { modifier = it }
    }

    companion object : SimpleGameContext("tgttos") {
        override fun create(packet: ClientboundMccServerPacket): Game {
            return Tgttos(packet.types)
        }
    }
}
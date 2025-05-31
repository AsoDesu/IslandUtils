package dev.asodesu.islandutils.features.chatbuttons

import dev.asodesu.islandutils.api.Debounce
import dev.asodesu.islandutils.api.extentions.Resources
import dev.asodesu.islandutils.api.extentions.minecraft
import dev.asodesu.islandutils.api.game.activeGame
import dev.asodesu.islandutils.features.chatbuttons.button.ChannelButton
import dev.asodesu.islandutils.features.chatbuttons.button.SpriteChannelButton
import net.minecraft.util.ARGB
import kotlin.time.Duration.Companion.seconds

object ChatButtons {
    val UNDERLINE_COLOR = ARGB.white(1f)
    val BUTTON_WIDTH = 43
    val BUTTON_HEIGHT = 9
    val INPUT_GAP = 2
    val BUTTON_GAP = 3

    val LOCAL = SpriteChannelButton("local", Resources.islandUtils("chat_button/local"))
    val PARTY = SpriteChannelButton("party", Resources.islandUtils("chat_button/party"))
    val TEAM = SpriteChannelButton("team", Resources.islandUtils("chat_button/team"))
    val PLOBBY = SpriteChannelButton("plobby", Resources.islandUtils("chat_button/plobby"))

    private val channelCommandDebouce = Debounce(1.seconds)

    fun currentButtons(): List<ChannelButton> {
        val buttons = mutableListOf<ChannelButton>(LOCAL, PARTY)
        if (activeGame.hasTeamChat) buttons.add(TEAM)
        // TODO: Plobby state detection

        return buttons
    }

    fun switchChannel(channel: String) {
        if (!channelCommandDebouce.consume()) return
        minecraft.connection?.sendCommand("chat $channel")
    }

}
package dev.asodesu.islandutils.features.chatbuttons.button

import dev.asodesu.islandutils.features.chatbuttons.ChatButtons
import net.minecraft.client.gui.components.AbstractWidget

interface ChannelButton {
    val channel: String
    fun widget(): AbstractWidget
    fun onPress() = ChatButtons.switchChannel(channel)
}
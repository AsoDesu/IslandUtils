package dev.asodesu.islandutils.api.events

import com.noxcrew.noxesium.core.mcc.ClientboundMccGameStatePacket
import com.noxcrew.noxesium.core.mcc.ClientboundMccServerPacket
import dev.asodesu.islandutils.api.events.sound.SoundPlayCallback
import dev.asodesu.islandutils.api.events.sound.SoundStopCallback
import dev.asodesu.islandutils.api.events.sound.info.SoundInfo
import dev.asodesu.islandutils.api.game.Game
import net.minecraft.network.chat.Component

interface SingleEventConsumerDelegate : EventConsumer {
    val consumer: EventConsumer
    override fun onGameChange(from: Game, to: Game) {
        consumer.onGameChange(from, to)
    }
    override fun onGameStateUpdate(from: ClientboundMccGameStatePacket, to: ClientboundMccGameStatePacket) {
        consumer.onGameStateUpdate(from, to)
    }
    override fun onServerUpdate(packet: ClientboundMccServerPacket) {
        consumer.onServerUpdate(packet)
    }
    override fun onSidebarLine(component: Component) {
        consumer.onSidebarLine(component)
    }
    override fun onSoundPlay(info: SoundInfo, ci: SoundPlayCallback.Info) {
        consumer.onSoundPlay(info, ci)
    }
    override fun onSoundStop(info: SoundStopCallback.StopInfo, ci: SoundStopCallback.Info) {
        consumer.onSoundStop(info, ci)
    }
}
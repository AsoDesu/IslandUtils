package dev.asodesu.islandutils.api.events

import com.noxcrew.noxesium.core.mcc.ClientboundMccGameStatePacket
import com.noxcrew.noxesium.core.mcc.ClientboundMccServerPacket
import dev.asodesu.islandutils.api.events.sound.SoundPlayCallback
import dev.asodesu.islandutils.api.events.sound.SoundStopCallback
import dev.asodesu.islandutils.api.events.sound.info.SoundInfo
import dev.asodesu.islandutils.api.game.Game
import net.minecraft.network.chat.Component

interface MultiEventConsumer : EventConsumer {
    fun children(): Iterable<EventConsumer>
    override fun onGameChange(from: Game, to: Game) {
        children().forEach { it.onGameChange(from, to) }
    }
    override fun onGameStateUpdate(from: ClientboundMccGameStatePacket, to: ClientboundMccGameStatePacket) {
        children().forEach { it.onGameStateUpdate(from, to) }
    }
    override fun onServerUpdate(packet: ClientboundMccServerPacket) {
        children().forEach { it.onServerUpdate(packet) }
    }
    override fun onSidebarLine(component: Component) {
        children().forEach { it.onSidebarLine(component) }
    }
    override fun onSoundPlay(info: SoundInfo, ci: SoundPlayCallback.Info) {
        children().forEach { it.onSoundPlay(info, ci) }
    }
    override fun onSoundStop(info: SoundStopCallback.StopInfo, ci: SoundStopCallback.Info) {
        children().forEach { it.onSoundStop(info, ci) }
    }
}
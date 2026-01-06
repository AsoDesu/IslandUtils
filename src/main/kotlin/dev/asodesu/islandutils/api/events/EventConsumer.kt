package dev.asodesu.islandutils.api.events

import com.noxcrew.noxesium.core.mcc.ClientboundMccGameStatePacket
import com.noxcrew.noxesium.core.mcc.ClientboundMccServerPacket
import dev.asodesu.islandutils.api.events.sidebar.SidebarEvents
import dev.asodesu.islandutils.api.events.sidebar.SidebarLineUpdate
import dev.asodesu.islandutils.api.events.sound.SoundEvents
import dev.asodesu.islandutils.api.events.sound.SoundPlayCallback
import dev.asodesu.islandutils.api.events.sound.SoundStopCallback
import dev.asodesu.islandutils.api.events.sound.info.SoundInfo
import dev.asodesu.islandutils.api.game.Game
import dev.asodesu.islandutils.api.game.GameEvents
import dev.asodesu.islandutils.api.game.events.GameChangeCallback
import dev.asodesu.islandutils.api.game.events.GameStateUpdateCallback
import dev.asodesu.islandutils.api.game.events.ServerUpdateCallback
import net.minecraft.network.chat.Component

interface EventConsumer : GameChangeCallback, GameStateUpdateCallback, ServerUpdateCallback, SidebarLineUpdate, SoundPlayCallback, SoundStopCallback {
    fun registerEventHandlers() {
        GameEvents.GAME_CHANGE.register(this)
        GameEvents.GAME_STATE_UPDATE.register(this)
        GameEvents.SERVER_UPDATE.register(this)
        SidebarEvents.LINE_UPDATE.register(this)
        SoundEvents.SOUND_PLAY.register(this)
        SoundEvents.SOUND_STOP.register(this)
    }

    // GameEvents
    override fun onGameChange(from: Game, to: Game) {}
    override fun onGameStateUpdate(from: ClientboundMccGameStatePacket, to: ClientboundMccGameStatePacket) {}
    override fun onServerUpdate(packet: ClientboundMccServerPacket) {}

    // SidebarEvents
    override fun onSidebarLine(component: Component) {}

    // SoundEvents
    override fun onSoundPlay(info: SoundInfo, ci: SoundPlayCallback.Info) {}
    override fun onSoundStop(info: SoundStopCallback.StopInfo, ci: SoundStopCallback.Info) {}
}
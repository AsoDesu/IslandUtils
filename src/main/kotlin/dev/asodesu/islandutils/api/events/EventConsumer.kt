package dev.asodesu.islandutils.api.events

import com.noxcrew.noxesium.core.mcc.ClientboundMccGameStatePacket
import com.noxcrew.noxesium.core.mcc.ClientboundMccServerPacket
import dev.asodesu.islandutils.api.events.bossbar.BossbarContentsUpdate
import dev.asodesu.islandutils.api.events.bossbar.BossbarEvents
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
import dev.asodesu.islandutils.api.server.InstanceJoinCallback
import dev.asodesu.islandutils.api.server.ServerDisconnectCallback
import dev.asodesu.islandutils.api.server.ServerEvents
import dev.asodesu.islandutils.api.server.ServerJoinCallback
import java.util.*
import net.minecraft.network.chat.Component

interface EventConsumer :
    GameChangeCallback,
    GameStateUpdateCallback,
    ServerUpdateCallback,
    SidebarLineUpdate,
    SoundPlayCallback,
    SoundStopCallback,
    BossbarContentsUpdate,
    ServerJoinCallback,
    ServerDisconnectCallback,
    InstanceJoinCallback
{
    fun registerEventHandlers() {
        GameEvents.GAME_CHANGE.register(this)
        GameEvents.GAME_STATE_UPDATE.register(this)
        GameEvents.SERVER_UPDATE.register(this)
        SidebarEvents.LINE_UPDATE.register(this)
        SoundEvents.SOUND_PLAY.register(this)
        SoundEvents.SOUND_STOP.register(this)
        BossbarEvents.BOSSBAR_CONTENTS_UPDATE.register(this)
        ServerEvents.SERVER_JOIN.register(this)
        ServerEvents.INSTANCE_JOIN.register(this)
        ServerEvents.SERVER_DISCONNECT.register(this)
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

    // BossbarEvents
    override fun onBossbarContents(uuid: UUID, contents: Component) {}

    // ServerEvents
    override fun onServerConnect() {}
    override fun onInstanceSwitch() {}
    override fun onServerDisconnect() {}
}
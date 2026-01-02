package dev.asodesu.islandutils.api.game

import com.noxcrew.noxesium.core.mcc.ClientboundMccServerPacket
import com.noxcrew.noxesium.core.mcc.MccPackets
import dev.asodesu.islandutils.api.extentions.debug
import dev.asodesu.islandutils.api.game.context.GameContext
import dev.asodesu.islandutils.api.modules.Module

/**
 * Manager for managing the active game, listens for MCC Server packets
 * and updates active game accordingly
 *
 * @param contexts All the game contexts to be checked, can
 *  be ordered to define checking order
 */
class GameManager(vararg contexts: GameContext) : Module("GameManager") {
    private val registeredGames = contexts.toMutableList()
    var active: Game = EmptyGame

    override fun init() {
        logger.info("GameManager initialised with ${registeredGames.size} games.")

        MccPackets.CLIENTBOUND_MCC_SERVER.addListener(this, ClientboundMccServerPacket::class.java) { _, packet, _ ->
            this.onMccServer(packet)
        }
    }

    private fun onMccServer(packet: ClientboundMccServerPacket) {
        debug("Server: $packet")
        GameEvents.SERVER_UPDATE.invoker().onServerUpdate(packet)

        val context = registeredGames.firstOrNull { it.check(packet) }
        if (context == null) {
            resetGame()
            logger.error("Couldn't create game with data $packet")
            return
        }

        val newActiveGame = context.create(packet)
        active.unregisterEvents()
        active.unregister()
        GameEvents.GAME_CHANGE.invoker().onGameChange(active, newActiveGame)

        logger.info("Set active game to ${newActiveGame::class.simpleName}")
        active = newActiveGame
    }

    private fun resetGame() {
        logger.info("Reset active game")
        active.unregisterEvents()
        active.unregister()
        GameEvents.GAME_CHANGE.invoker().onGameChange(active, EmptyGame)

        active = EmptyGame
    }
}
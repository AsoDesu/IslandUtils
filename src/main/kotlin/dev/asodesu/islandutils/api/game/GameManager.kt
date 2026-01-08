package dev.asodesu.islandutils.api.game

import com.noxcrew.noxesium.core.mcc.ClientboundMccServerPacket
import com.noxcrew.noxesium.core.mcc.MccPackets
import dev.asodesu.islandutils.api.discord.DiscordManager
import dev.asodesu.islandutils.api.discord.build
import dev.asodesu.islandutils.api.events.EventConsumerWrapper
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
    val gamesById = registeredGames.associateBy { it.id }

    var active: Game = EmptyGame
    private val activeGameEventHandler = EventConsumerWrapper { active }
    var lastGameChange: Long = System.currentTimeMillis()

    override fun init() {
        logger.info("GameManager initialised with ${registeredGames.size} games.")

        activeGameEventHandler.registerEventHandlers()
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
            debug("Couldn't create game with data $packet")
            return
        }
        val newActiveGame = context.create(packet)
        setGame(newActiveGame)
    }

    fun setGame(newActiveGame: Game) {
        active.unregister()
        GameEvents.GAME_CHANGE.invoker().onGameChange(active, newActiveGame)
        if (active::class != newActiveGame::class) {
            lastGameChange = System.currentTimeMillis()
        }

        debug("Set active game to $newActiveGame")
        active = newActiveGame
        this.pushDiscord()
    }

    fun pushDiscord() {
        try {
            DiscordManager.pushContainer(active.discord.build())
        } catch (e: Exception) {
            DiscordManager.resetContainer()
        }
    }

    private fun resetGame() {
        debug("Reset active game")
        active.unregister()
        GameEvents.GAME_CHANGE.invoker().onGameChange(active, EmptyGame)

        active = EmptyGame
        this.pushDiscord()
    }
}
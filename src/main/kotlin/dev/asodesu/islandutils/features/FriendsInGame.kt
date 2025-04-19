package dev.asodesu.islandutils.features

import dev.asodesu.islandutils.api.Scheduler
import dev.asodesu.islandutils.api.Scheduler.runAfter
import dev.asodesu.islandutils.api.audience
import dev.asodesu.islandutils.api.connection
import dev.asodesu.islandutils.api.game.GameEvents
import dev.asodesu.islandutils.api.game.inLobby
import dev.asodesu.islandutils.api.minecraft
import dev.asodesu.islandutils.api.modules.Module
import dev.asodesu.islandutils.api.send
import net.minecraft.network.protocol.game.ClientboundCommandSuggestionsPacket.Entry
import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket
import kotlin.time.Duration.Companion.seconds

object FriendsInGame : Module("FriendsInGame") {
    const val TRANSACTION_ID = 6775161

    var enabledInGame = true // TODO: CONFIG API
    var enabledInLobby = true // TODO: CONFIG API

    var friends = listOf<String>()

    override fun init() {
        GameEvents.SERVER_UPDATE.register {
            if (!enabledInLobby && !enabledInGame) return@register
            val commandSuggestion = ServerboundCommandSuggestionPacket(TRANSACTION_ID, "/friend remove")
            minecraft.connection?.send(commandSuggestion)
        }
    }

    // called from mixin
    fun receiveSuggestionCallback(entries: List<Entry>) {
        friends = entries.map { it.text }
        if (inLobby && !enabledInLobby) return // return if we're in lobby and lobby is disabled
        if (!enabledInGame) return // we're in game, if it's disabled in game, return

        runAfter(1.75.seconds) { sendFriends() }
    }

    private fun sendFriends() {
        val connection = connection ?: return
        // create a list of the usernames of everyone on this server who is friends
        val onlineFriendNames = connection.onlinePlayers.mapNotNull map@ {
            val name = it.profile.name
            if (!friends.contains(name)) null
            else name
        }
        if (onlineFriendNames.isEmpty()) return

        val friendString = onlineFriendNames.joinToString(", ")

        val lang = if (inLobby) "lobby" else "game"
        audience.send(
            "<green>[<white>S</white>] <lang:islandutils.feature.friends.$lang>:</green> <yellow>$friendString"
        )
    }
}
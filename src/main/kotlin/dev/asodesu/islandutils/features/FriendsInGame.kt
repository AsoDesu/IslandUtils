package dev.asodesu.islandutils.features

import dev.asodesu.islandutils.api.Font
import dev.asodesu.islandutils.api.Scheduler.runAfter
import dev.asodesu.islandutils.api.buildComponent
import dev.asodesu.islandutils.api.connection
import dev.asodesu.islandutils.api.game.GameEvents
import dev.asodesu.islandutils.api.game.inLobby
import dev.asodesu.islandutils.api.minecraft
import dev.asodesu.islandutils.api.modules.Module
import dev.asodesu.islandutils.api.send
import dev.asodesu.islandutils.options.MiscOptions
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.game.ClientboundCommandSuggestionsPacket.Entry
import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket
import kotlin.time.Duration.Companion.seconds

object FriendsInGame : Module("FriendsInGame") {
    const val TRANSACTION_ID = 6775161

    private val enabledInGame by MiscOptions.FriendsInGame.inGame
    private val enabledInLobby by MiscOptions.FriendsInGame.inLobby

    var friends = listOf<String>()

    override fun init() {
        GameEvents.SERVER_UPDATE.register {
            if (!enabledInLobby && !enabledInGame) return@register
            val commandSuggestion = ServerboundCommandSuggestionPacket(TRANSACTION_ID, "/friend remove ")
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

        val component = buildComponent {
            append(Component.literal("[").append(Font.SOCIAL_ICON).append("] "))
            append(Component.translatable("islandutils.feature.friends.$lang").append(": "))
            append(Component.literal(friendString).withStyle(ChatFormatting.YELLOW))
        }

        send(component)
    }
}
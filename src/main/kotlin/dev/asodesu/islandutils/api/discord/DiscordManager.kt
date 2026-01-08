package dev.asodesu.islandutils.api.discord

import dev.asodesu.islandutils.api.discord.container.DiscordContainer
import dev.asodesu.islandutils.api.discord.container.EmptyDiscordContainer
import dev.asodesu.islandutils.api.events.MultiEventConsumerWrapper
import dev.asodesu.islandutils.api.extentions.debug
import dev.asodesu.islandutils.api.modules.Module
import dev.asodesu.islandutils.api.server.ServerEvents
import dev.asodesu.islandutils.api.server.ServerSessionHandler
import dev.asodesu.islandutils.options.DiscordOptions
import io.github.vyfor.kpresence.RichClient
import io.github.vyfor.kpresence.rpc.Activity

object DiscordManager : Module("DiscordPresence") {
    val client = RichClient(1027930697417101344)
    private val overrides = mutableListOf<DiscordContainerOverride>()

    private val containerEventWrapper = MultiEventConsumerWrapper { container.eventChildren() }
    private var container: DiscordContainer = DiscordContainer.empty
        set(value) {
            field = value
            this.update()
        }

    override fun init() {
        containerEventWrapper.registerEventHandlers()
        client.connect(shouldBlock = false)

        addOverride(
            reason = "disabled in config",
            priority = 10,
            check = { !DiscordOptions.enabled.get() },
            create = { EmptyDiscordContainer }
        )
        addOverride(
            reason = "big rat",
            priority = 9,
            check = { !ServerSessionHandler.isProduction },
            create = { DiscordContainer.bigRat }
        )
        addOverride(
            reason = "disabled place",
            priority = 8,
            check = { !DiscordOptions.showPlace.get() },
            create = { DiscordContainer.default.build() }
        )

        ServerEvents.SERVER_DISCONNECT.register {
            resetContainer()
        }
    }

    fun addOverride(reason: String, priority: Int, check: () -> Boolean, create: () -> DiscordContainer) {
        overrides.add(DiscordContainerOverride(reason, priority, check, create))
        overrides.sortBy { it.priority }
    }
    fun updateOverride(): Boolean {
        val override = overrides.firstOrNull { it.check() }
        if (override != null) {
            this.container = override.create()
            debug("Discord activity overridden, ${override.reason}")
            return true
        }
        return false
    }

    fun pushContainer(container: DiscordContainer) {
        if (updateOverride()) return

        this.container = container
        debug("Updated Discord activity to $container")
    }
    fun resetContainer() {
        this.container = DiscordContainer.empty
        debug("Reset Discord activity")
    }

    fun update() = container.update()

    fun updateActivity(activity: Activity) {
        if (!DiscordOptions.enabled.get()) return
        client.update(activity)
        logger.info("Updated activity")
    }
    fun clearActivity() {
        client.clear()
        logger.info("Cleared activity")
    }
}
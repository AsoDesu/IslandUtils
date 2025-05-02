package dev.asodesu.islandutils.features.crafting.notif

import dev.asodesu.islandutils.Sounds
import dev.asodesu.islandutils.api.isOnline
import dev.asodesu.islandutils.api.minecraft
import dev.asodesu.islandutils.api.modules.Module
import dev.asodesu.islandutils.api.play
import dev.asodesu.islandutils.features.crafting.items.SavedCraftingItems
import dev.asodesu.islandutils.options.NotificationOptions
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.Minecraft

object CraftingNotifier : Module("CraftingNotifier") {
    private var updateTicks = 0
    private val enabled by NotificationOptions.craftingNotifications

    override fun init() {
        SavedCraftingItems.init()
        ClientTickEvents.END_CLIENT_TICK.register(::tick)
    }

    fun tick(client: Minecraft) {
        if (!enabled) return
        if (!isOnline || client.overlay != null) return
        if (++updateTicks < 20) return

        val toSend = SavedCraftingItems.items.filter { !it.notified && it.isComplete() }
        if (toSend.isEmpty()) return

        toSend.forEach {
            it.notified = true
            client.toastManager.addToast(CraftingToast(it))
        }
        minecraft.soundManager.play(Sounds.UI_ACHIVEMENT_RECEIVE)
        SavedCraftingItems.save()
    }
}
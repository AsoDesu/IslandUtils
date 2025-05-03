package dev.asodesu.islandutils.features.crafting.notif

import dev.asodesu.islandutils.Font
import dev.asodesu.islandutils.Sounds
import dev.asodesu.islandutils.api.extentions.buildComponent
import dev.asodesu.islandutils.api.extentions.isOnline
import dev.asodesu.islandutils.api.extentions.minecraft
import dev.asodesu.islandutils.api.extentions.newLine
import dev.asodesu.islandutils.api.extentions.play
import dev.asodesu.islandutils.api.extentions.send
import dev.asodesu.islandutils.api.modules.Module
import dev.asodesu.islandutils.api.notifier.Notification
import dev.asodesu.islandutils.api.notifier.NotificationSource
import dev.asodesu.islandutils.api.notifier.Notifier
import dev.asodesu.islandutils.features.crafting.items.CraftingItem
import dev.asodesu.islandutils.features.crafting.items.SavedCraftingItems
import dev.asodesu.islandutils.options.NotificationOptions
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import org.apache.commons.lang3.time.DurationFormatUtils

object CraftingNotifier : Module("CraftingNotifier"), NotificationSource {
    private var updateTicks = 0
    private val enabled by NotificationOptions.craftingNotifications
    override val title: MutableComponent = Component.translatable("islandutils.notifications.crafting.complete")

    override fun init() {
        SavedCraftingItems.init()
        Notifier.add(this)
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
            send(chatNotification(it))
        }
        minecraft.soundManager.play(Sounds.UI_ACHIVEMENT_RECEIVE)
        SavedCraftingItems.save()
    }

    private fun chatNotification(item: CraftingItem): Component {
        return buildComponent {
            withStyle { it.withColor(ChatFormatting.DARK_GREEN) }

            append(Component.literal("(").append(item.type.icon).append(") ")) // prefix
            append(Component.translatable("islandutils.feature.crafting.chat.notification", item.item.hoverName))
        }
    }

    fun commandResponse(): Component {
        return buildComponent {
            newLine()
            append(Component.translatable("islandutils.feature.crafting.chat.list").withStyle(Font.HUD_STYLE))

            SavedCraftingItems.items.forEach { item ->
                val time = if (!item.isComplete()) {
                    val timeRemaining =  item.finishTimestamp - System.currentTimeMillis()
                    val duration = DurationFormatUtils.formatDuration(timeRemaining, "H'h' m'm' s's'")
                    Component.literal(duration).withStyle(ChatFormatting.RED)
                } else {
                    Component.translatable("islandutils.feature.crafting.chat.complete").withStyle(ChatFormatting.DARK_GREEN)
                }

                newLine()
                append(Component.translatable(
                    "islandutils.feature.crafting.chat.item",
                    item.type.icon, item.item.hoverName, time
                ))
            }
        }
    }

    override fun provide() = SavedCraftingItems.items.mapNotNull {
        if (!it.isComplete()) null else Notification(it.item.hoverName, this)
    }
}
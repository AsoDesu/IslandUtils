package dev.asodesu.islandutils.features

import com.mojang.blaze3d.platform.InputConstants
import dev.asodesu.islandutils.IslandUtils
import dev.asodesu.islandutils.api.Debounce
import dev.asodesu.islandutils.api.extentions.isOnline
import dev.asodesu.islandutils.api.modules.Module
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class CommandKeybind(
    keybindName: String,
    key: Int,
    private val command: String,
    type: InputConstants.Type = InputConstants.Type.KEYSYM,
    debounceTime: Duration = 1.seconds
) : Module("CmdKeybind-$keybindName") {
    private val keyMapping = KeyMapping(
        "key.islandutils.$keybindName",
        type,
        key,
        IslandUtils.keyMappingCategory
    )
    private val debounce = Debounce(debounceTime)

    override fun init() {
        KeyBindingHelper.registerKeyBinding(keyMapping)
        ClientTickEvents.END_CLIENT_TICK.register(::tick)
    }

    private fun tick(client: Minecraft) {
        if (!keyMapping.consumeClick()) return

        val player = client.player?.connection ?: return
        if (!isOnline || !debounce.consume()) return

        player.sendCommand(command)
    }
}
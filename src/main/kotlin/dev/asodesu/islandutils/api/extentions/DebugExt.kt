package dev.asodesu.islandutils.api.extentions

import com.mojang.blaze3d.platform.InputConstants
import dev.asodesu.islandutils.api.chest.analysis.ContainerScreenHelper
import dev.asodesu.islandutils.api.chest.customItemId
import dev.asodesu.islandutils.options.MiscOptions
import java.util.function.Consumer
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import org.slf4j.LoggerFactory

val debugMode by MiscOptions.Debug.debugMode

private val logger = LoggerFactory.getLogger("IU-DEBUG")
fun debug(str: String) {
    if (debugMode) send(Component.literal("[IslandUtils] $str").withStyle(ChatFormatting.GRAY))
    else logger.info(str)
}

fun addDebugTooltip(item: ItemStack, consumer: Consumer<Component>) {
    if (!debugMode) return
    if (!InputConstants.isKeyDown(minecraft.window.window, InputConstants.KEY_LCONTROL)) return

    val customItemId = item.customItemId
    consumer.accept(buildComponent {
        // add custom item id
        if (customItemId != null) {
            append(Component.literal(customItemId.toString()).style { withColor(ChatFormatting.AQUA) })
        }

        // get the current container screen
        if (minecraft.screen is AbstractContainerScreen<*>) {
            val hoveredSlot = (minecraft.screen as ContainerScreenHelper).getHoveredSlot() ?: return@buildComponent
            if (hoveredSlot.item == item) {
                if (customItemId != null) append(" ")
                append(Component.literal("(${hoveredSlot.containerSlot})").style { withColor(ChatFormatting.GRAY) })
            }
        }
    })
}
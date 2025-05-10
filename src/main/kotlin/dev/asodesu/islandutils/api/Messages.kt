package dev.asodesu.islandutils.api

import dev.asodesu.islandutils.api.extentions.buildComponent
import dev.asodesu.islandutils.api.extentions.style
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextColor

object Messages {

    val ACTION_PREFIX_COLOR = TextColor.parseColor("#505050").orThrow
    val ACTION_DESCRIPTION_COLOR = TextColor.parseColor("#ECD584").orThrow
    val ACTION_TRIGGER_COLOR = TextColor.parseColor("#FEE761").orThrow
    fun action(actionIcons: Component, action: String, trigger: String): Component {
        return buildComponent {
            append(actionIcons)
            append(Component.literal(" > ").style { withColor(ACTION_PREFIX_COLOR) })
            append(Component.literal("$action to ").style { withColor(ACTION_DESCRIPTION_COLOR) })
            append(Component.literal(trigger).style { withColor(ACTION_TRIGGER_COLOR) })
        }
    }

}
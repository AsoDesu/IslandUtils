package dev.asodesu.islandutils.features.scavenging

import dev.asodesu.islandutils.api.chest.font.Icons
import dev.asodesu.islandutils.api.extentions.Resources
import dev.asodesu.islandutils.api.extentions.buildComponent
import dev.asodesu.islandutils.api.extentions.style
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component

interface CurrencyRenderer {
    fun apply(id: String): Boolean
    fun render(total: Int): Component?
    fun renderTooltip(total: Int): Component

    companion object {
        fun formatTotal(total: Int): Component {
            return Component.literal("%,d".format(total)).style { withColor(ChatFormatting.WHITE) }
        }

        fun simpleIcon(icon: String) = Simple(Icons.getCharacter(Resources.mcc(icon))!!)
    }

    class Unknown(private val currencyComponent: Component) : CurrencyRenderer {
        override fun apply(id: String) = true
        override fun render(total: Int) = null
        override fun renderTooltip(total: Int) = buildComponent {
            append(formatTotal(total))
            append(" ")
            append(currencyComponent)
        }
    }

    class Simple(private val matchIcon: String, private val currencyComponent: Component = Icons.toIcon(matchIcon)) : CurrencyRenderer {
        override fun apply(id: String) = id.endsWith(matchIcon)

        override fun render(total: Int): Component {
            return buildComponent {
                append(formatTotal(total))
                append(" ")
                append(currencyComponent)
            }
        }

        override fun renderTooltip(total: Int) = render(total)
    }
}
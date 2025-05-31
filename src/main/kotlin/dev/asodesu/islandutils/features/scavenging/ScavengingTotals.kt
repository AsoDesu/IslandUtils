package dev.asodesu.islandutils.features.scavenging

import dev.asodesu.islandutils.Font
import dev.asodesu.islandutils.api.Messages
import dev.asodesu.islandutils.api.chest.analysis.ChestAnalyser
import dev.asodesu.islandutils.api.chest.analysis.ChestAnalyserFactory
import dev.asodesu.islandutils.api.chest.analysis.ContainerScreenHelper
import dev.asodesu.islandutils.api.chest.loreOrNull
import dev.asodesu.islandutils.api.extentions.Resources
import dev.asodesu.islandutils.api.extentions.appendLine
import dev.asodesu.islandutils.api.extentions.buildComponent
import dev.asodesu.islandutils.api.extentions.debug
import dev.asodesu.islandutils.api.extentions.newLine
import dev.asodesu.islandutils.api.extentions.pose
import dev.asodesu.islandutils.api.extentions.style
import dev.asodesu.islandutils.options.MiscOptions
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextColor
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import dev.asodesu.islandutils.api.extentions.minecraft as minecraftClient

class ScavengingTotals : ChestAnalyser {
    private val SLOTS_1 = 11..15
    private val SLOTS_2 = 20..25
    private val CONFIRM_BUTTON_SLOTS = 64..66

    private val SCAVENGES_TITLE = "Scavenges Into"
    private val SCAVENGING_TITLE_COLOR = TextColor.parseColor("#65FFFF").orThrow
    private val CURRENCY_TOTAL_COLOR = TextColor.parseColor("#505050").orThrow
    private val CURRENCY_TOTAL_REGEX = "(([\\d,])+)".toRegex()

    private val MCC_MENU_WIDTH = 176
    private val MCC_MENU_Y_OFFSET = -20
    private val MCC_HEADER_TO_FOOTER_HEIGHT = 154
    private val MCC_FOOTER_PADDING = 24

    private val CHEST_MENU_HEIGHT = 222
    val minecraft = minecraftClient

    // currencies we render to the screen
    private val CURRENCY_RENDERERS = listOf(
        CurrencyRenderer.simpleIcon("_fonts/silver.png"),
        CurrencyRenderer.simpleIcon("_fonts/coin_small.png"),
        CurrencyRenderer.simpleIcon("_fonts/material_dust.png"),
        CurrencyRenderer.simpleIcon("_fonts/royal_reputation.png")
    )

    private val scavengingItems = mutableMapOf<Int, ScavengingItem>()
    private var currencies = listOf<ScavengingCurrency>()

    private var totalsTooltip: Component? = null
    private var screenComponent: Component? = null
    private var dirty = false

    override fun analyse(item: ItemStack, slot: Int) {
        if (slot !in SLOTS_1 && slot !in SLOTS_2) return
        val lores = item.loreOrNull
        if (lores == null) {
            scavengingItems.remove(slot)
            dirty = true
            return
        }

        // https://img.lukynka.cloud/wtf.png
        val scavengingItem = ScavengingItem()
        var foundTitle = false
        lores.forEach line@{ line ->
            // first we need to find the "Scavenges Into:" text
            if (!foundTitle && line.string.contains(SCAVENGES_TITLE)) {
                foundTitle = true
            } else {
                var amount: Int? = null
                val currency = Component.empty()

                // search over all components in this lore line
                line.siblings.forEach lineComponent@{
                    // if we haven't found numbers for the amount yet, we need to find them
                    if (amount == null) {
                        // check for the currency total, being plain white text that matches CURRENCY_TOTAL_REGEX
                        if (it.style.color == null) {
                            // check if this component is JUST numbers
                            val result = CURRENCY_TOTAL_REGEX.find(it.string.trim()) ?: return@lineComponent
                            amount = result.groupValues[1].replace(",", "").toIntOrNull() ?: return@lineComponent
                        }
                    } else {
                        // we previously found an amount numbers, therefore everything after must be a
                        //  space and then the currency icon
                        currency.append(it)
                    }
                }

                // if we didn't find any numbers this line then we've probably found the end of the rewards for this item
                if (amount == null) {
                    foundTitle = false
                } else {
                    scavengingItem.rewards += ScavengingItem.Reward(currency.string, currency, amount!!)
                }
            }
        }
        scavengingItems[slot] = scavengingItem
        dirty = true
    }

    private fun updateScreenComponents() {
        dirty = false
        debug("Updating scavenging tooltip")

        if (scavengingItems.isEmpty()) {
            totalsTooltip = null
            screenComponent = null
            return
        }

        currencies = collect()
        totalsTooltip = buildComponent {
            appendLine(Component.translatable("islandutils.feature.scavenging.manual"))
            newLine()

            appendLine(Component.translatable("islandutils.feature.scavenging.into").style { withColor(SCAVENGING_TITLE_COLOR) })
            currencies.forEach { currency ->
                val currencyTooltip = currency.renderer.renderTooltip(currency.total)
                appendLine(
                    Component.translatable("islandutils.feature.scavenging.reward", currencyTooltip)
                        .style { withColor(CURRENCY_TOTAL_COLOR) }
                )
            }
            newLine()
            append(Messages.action(Font.ACTION_CLICK_LEFT, "Click", "Scavenge Items"))
        }

        screenComponent = buildComponent {
            currencies.forEach { currency ->
                val screenComponent = currency.renderer.render(currency.total) ?: return@forEach
                append(" ")
                append(screenComponent)
            }
        }
    }

    private fun collect(): List<ScavengingCurrency> {
        val currencies = scavengingItems.flatMap { it.value.rewards }.groupBy { it.id }
        return currencies.map { (id, items) ->
            val renderer = CURRENCY_RENDERERS.firstOrNull { it.apply(id) }
                ?: CurrencyRenderer.Unknown(items.first().component)
            val total = items.sumOf { it.total }
            ScavengingCurrency(total, renderer)
        }.sortedBy { it.total }
    }

    override fun render(guiGraphics: GuiGraphics, helper: ContainerScreenHelper) {
        if (dirty) updateScreenComponents()
        val screen = helper.getScreen()
        if (helper.getHoveredSlot()?.index in CONFIRM_BUTTON_SLOTS) {
            totalsTooltip?.let { screen.setTooltipForNextRenderPass(it) }
        }

        screenComponent?.let { component ->
            val x = ((screen.width - MCC_MENU_WIDTH) / 2) + MCC_MENU_WIDTH - MCC_FOOTER_PADDING - minecraft.font.width(component)

            val footerContainerOffset = (MCC_FOOTER_PADDING - minecraft.font.lineHeight) / 2
            val y = ((screen.height - CHEST_MENU_HEIGHT) / 2) + MCC_MENU_Y_OFFSET + MCC_HEADER_TO_FOOTER_HEIGHT + footerContainerOffset + 1

            guiGraphics.pose {
                translate(0f, 0f, 105f)
                guiGraphics.drawString(minecraft.font, component, x, y, 16777215, false)
            }
        }
    }

    object Factory : ChestAnalyserFactory {
        private val MENU_COMPONENT = Resources.mcc("_fonts/body/scavenging.png")
        private val enabled by MiscOptions.scavengingTotals

        override fun shouldApply(menuComponents: Collection<ResourceLocation>): Boolean {
            return enabled && menuComponents.contains(MENU_COMPONENT)
        }

        override fun create(menuComponents: Collection<ResourceLocation>) = ScavengingTotals()
    }
}
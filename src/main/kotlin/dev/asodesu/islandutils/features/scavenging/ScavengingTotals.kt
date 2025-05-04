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
import dev.asodesu.islandutils.api.extentions.style
import dev.asodesu.islandutils.options.MiscOptions
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextColor
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack

class ScavengingTotals : ChestAnalyser {
    private val SLOTS_1 = 11..15
    private val SLOTS_2 = 20..25
    private val CONFIRM_BUTTON_SLOTS = 64..66

    private val SCAVENGES_TITLE = "Scavenges Into"
    private val SCAVENGING_TITLE_COLOR = TextColor.parseColor("#65FFFF").orThrow
    private val CURRENCY_TOTAL_COLOR = TextColor.parseColor("#505050").orThrow
    private val CURRENCY_TOTAL_REGEX = "(([\\d,])+)".toRegex()

    private val scavengingItems = mutableMapOf<Int, ScavengingItem>()
    private var currencies = listOf<ScavengingItem>()

    private var totalsTooltip: Component? = null
    private var dirtyTooltip = false

    override fun analyse(item: ItemStack, slot: Int) {
        if (slot !in SLOTS_1 && slot !in SLOTS_2) return
        val lores = item.loreOrNull
        if (lores == null) {
            scavengingItems.remove(slot)
            dirtyTooltip = true
            return
        }

        // https://img.lukynka.cloud/wtf.png
        var foundTitle = false
        lores.forEach line@{ line ->
            // we are BELOW the "Scavenges Into:" text
            if (foundTitle) {
                var amount: Int? = null
                val currency = Component.empty()

                // search over all components in this lore line
                line.siblings.forEach lineComponent@{
                    // we have found an amount before this... therefore the next components must be the currency
                    if (amount != null) {
                        currency.append(it)
                    } else {
                        // check for the currency total, being pain white text that matches CURRENCY_TOTAL_REGEX
                        if (it.style.color == null) {
                            val result = CURRENCY_TOTAL_REGEX.find(it.string) ?: return@lineComponent
                            amount = result.groupValues[1].replace(",", "").toIntOrNull() ?: return@lineComponent
                        }
                    }
                }

                // if we didn't find any numbers this line then we've probably found the end of the rewards for this item
                if (amount == null) {
                    foundTitle = false
                } else {
                    scavengingItems[slot] = ScavengingItem(currency.string, currency, amount!!)
                    dirtyTooltip = true
                }
            } else if (line.string.contains(SCAVENGES_TITLE)) {
                // we found the Scavenges Into: text
                foundTitle = true
            }
        }
    }

    private fun updateTooltip() {
        dirtyTooltip = false
        debug("Updating scavenging tooltip")

        if (scavengingItems.isEmpty()) totalsTooltip = null

        currencies = collect()
        totalsTooltip = buildComponent {
            appendLine(Component.translatable("islandutils.feature.scavenging.manual"))
            newLine()

            appendLine(Component.translatable("islandutils.feature.scavenging.into").style { withColor(SCAVENGING_TITLE_COLOR) })
            currencies.forEach { currency ->
                val totalString = Component.literal("%,d".format(currency.total)).style { withColor(ChatFormatting.WHITE) }

                appendLine(
                    Component.translatable("islandutils.feature.scavenging.reward", totalString, currency.currencyComponent)
                        .style { withColor(CURRENCY_TOTAL_COLOR) }
                )
            }
            newLine()
            append(Messages.action(Font.ACTION_CLICK_LEFT, "Click", "Scavenge Items"))
        }
    }

    private fun collect(): List<ScavengingItem> {
        val currencies = scavengingItems.values.groupBy { it.currencyId }
        return currencies.map { (id, items) ->
            val component = items.first().currencyComponent
            val total = items.sumOf { it.total }
            ScavengingItem(id, component, total)
        }.sortedBy { it.total }
    }

    override fun render(guiGraphics: GuiGraphics, helper: ContainerScreenHelper) {
        if (dirtyTooltip) updateTooltip()
        if (helper.getHoveredSlot()?.index in CONFIRM_BUTTON_SLOTS) {
            totalsTooltip?.let { helper.getScreen().setTooltipForNextRenderPass(it) }
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
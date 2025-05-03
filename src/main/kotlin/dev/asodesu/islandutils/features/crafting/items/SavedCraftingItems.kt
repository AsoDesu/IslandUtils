package dev.asodesu.islandutils.features.crafting.items

import dev.asodesu.islandutils.api.extentions.islandUtilsFolder
import dev.asodesu.islandutils.api.json.decode
import dev.asodesu.islandutils.api.json.encode
import dev.asodesu.islandutils.api.modules.Module
import dev.asodesu.islandutils.features.crafting.CraftingMenuType
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.Minecraft
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

object SavedCraftingItems : Module("CraftingItemsStorage") {
    private val file: Path = islandUtilsFolder.resolve("crafting.json")
    private var dirty = false
    var items = mutableListOf<CraftingItem>()

    override fun init() {
        this.load()
        ClientTickEvents.END_CLIENT_TICK.register(::tick)
    }

    fun tick(client: Minecraft) {
        if (dirty) {
            dirty = false
            CompletableFuture.runAsync(::save)
        }
    }

    fun add(item: CraftingItem) {
        remove(item.type, item.slot)
        items += item
        dirty = true
    }

    fun remove(type: CraftingMenuType, slot: Int) {
        items.removeIf { it.type == type && it.slot == slot }
        dirty = true
    }

    fun load() {
        if (!file.exists()) return logger.info("No crafting items to load.")
        val json = file.readText()
        try {
            items = json.decode<List<CraftingItem>>().toMutableList()
        } catch (e: Exception) {
            logger.error("Failed to load crafting items", e)
        }
        dirty = false
    }

    fun save() {
        val json = try {
            items.encode()
        } catch (e: Exception) {
            logger.error("Failed to save crafting items", e)
            return
        }

        file.writeText(json)
        logger.info("Saved crafting items")
        dirty = false
    }
}
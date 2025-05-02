package dev.asodesu.islandutils.api.chest.analysis

import dev.asodesu.islandutils.api.modules.Module
import net.minecraft.resources.ResourceLocation

class ChestAnalysisManager(vararg factories: ChestAnalyserFactory) : Module("ChestAnalysis") {
    private val analysers = factories.toList()

    override fun init() {
    }

    fun createAnalyser(menuComponents: Collection<ResourceLocation>): ChestAnalyser? {
        val factories = analysers.filter {
            try {
                it.shouldApply(menuComponents)
            } catch (e: Exception) {
                logger.error("Error checking apply on ChestAnalyserFactory $it", e)
                false
            }
        }

        return if (factories.isEmpty()) null
        else if (factories.size == 1) {
            factories[0].createCatching(menuComponents)
        } else {
            MultiChestAnalyser(factories.mapNotNull { it.createCatching(menuComponents) })
        }
    }

    private fun ChestAnalyserFactory.createCatching(menuComponents: Collection<ResourceLocation>): ChestAnalyser? {
        return try {
            create(menuComponents)
        } catch (e: Exception) {
            logger.error("Error creating ChestAnalyser $this", e)
            return null
        }
    }
}
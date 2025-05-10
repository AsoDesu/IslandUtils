package dev.asodesu.islandutils.api.chest.analysis

import dev.asodesu.islandutils.api.modules.Module
import net.minecraft.resources.ResourceLocation

class ChestAnalysisManager(factories: List<ChestAnalyserFactory>, val analysers: List<ChestAnalyser>) : Module("ChestAnalysis") {
    private val factories = factories.toList()

    override fun init() {
    }

    fun createAnalyser(menuComponents: Collection<ResourceLocation>): ChestAnalyser? {
        val factories = factories.filter {
            try {
                it.shouldApply(menuComponents)
            } catch (e: Exception) {
                logger.error("Error checking apply on ChestAnalyserFactory $it", e)
                false
            }
        }

        return MultiChestAnalyser(buildList {
            factories.forEach { factory ->
                val analyser = factory.createCatching(menuComponents) ?: return@forEach
                add(analyser)
            }
            addAll(analysers)
        })
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
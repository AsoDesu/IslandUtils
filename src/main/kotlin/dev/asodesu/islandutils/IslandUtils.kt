package dev.asodesu.islandutils

import dev.asodesu.islandutils.api.Scheduler
import dev.asodesu.islandutils.api.modules.ModuleManager
import net.fabricmc.api.ModInitializer

object IslandUtils : ModInitializer {
	override fun onInitialize() {
		Scheduler.init()
		ModuleManager.init()
	}
}
package dev.asodesu.islandutils

import dev.asodesu.islandutils.api.Scheduler
import dev.asodesu.islandutils.api.modules.ModuleManager
import dev.asodesu.islandutils.options.Options
import net.fabricmc.api.ModInitializer

object IslandUtils : ModInitializer {
	override fun onInitialize() {
		Options.load()

		Scheduler.init()
		ModuleManager.init()
	}
}
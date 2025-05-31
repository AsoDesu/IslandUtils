package dev.asodesu.islandutils

import dev.asodesu.islandutils.api.Scheduler
import dev.asodesu.islandutils.api.extentions.islandUtilsFolder
import dev.asodesu.islandutils.api.modules.ModuleManager
import dev.asodesu.islandutils.features.crafting.craftingCommand
import dev.asodesu.islandutils.options.Options
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import kotlin.io.path.createDirectories
import kotlin.io.path.exists

object IslandUtils : ModInitializer {

	override fun onInitialize() {
		if (!islandUtilsFolder.exists()) islandUtilsFolder.createDirectories()

		Options.load()

		Scheduler.init()
		ModuleManager.init()

		ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
			dispatcher.register(islandUtilsCommand())
			dispatcher.register(craftingCommand())
		}
	}
}
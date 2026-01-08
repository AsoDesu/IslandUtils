package dev.asodesu.islandutils

import com.noxcrew.noxesium.core.fabric.mcc.MccNoxesiumEntrypoint
import dev.asodesu.islandutils.api.Scheduler
import dev.asodesu.islandutils.api.extentions.Resources
import dev.asodesu.islandutils.api.extentions.islandUtilsFolder
import dev.asodesu.islandutils.api.modules.ModuleManager
import dev.asodesu.islandutils.features.crafting.craftingCommand
import dev.asodesu.islandutils.options.Options
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.ModContainer
import net.minecraft.client.KeyMapping
import org.slf4j.LoggerFactory
import kotlin.io.path.createDirectories
import kotlin.io.path.exists

object IslandUtils : MccNoxesiumEntrypoint() {
	val keyMappingCategory = KeyMapping.Category.register(Resources.islandUtils("islandutils"))
	val logger = LoggerFactory.getLogger("IslandUtils")

	val modId = "islandutils"
	lateinit var modContainer: ModContainer
	lateinit var modVersion: String

	override fun initialize() {
		if (!islandUtilsFolder.exists()) islandUtilsFolder.createDirectories()
		this.modContainer = FabricLoader.getInstance().getModContainer(modId).get()
		this.modVersion = this.modContainer.metadata.version.friendlyString

		Options.load()

		Scheduler.init()
		ModuleManager.init()

		ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
			dispatcher.register(islandUtilsCommand())
			dispatcher.register(craftingCommand())
		}
	}
}
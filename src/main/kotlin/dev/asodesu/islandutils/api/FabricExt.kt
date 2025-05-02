package dev.asodesu.islandutils.api

import java.nio.file.Path
import net.fabricmc.loader.api.FabricLoader

val configDir: Path = FabricLoader.getInstance().configDir
val islandUtilsFolder: Path = configDir.resolve("islandutils")
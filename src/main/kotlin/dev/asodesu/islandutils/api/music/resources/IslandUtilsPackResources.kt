package dev.asodesu.islandutils.api.music.resources

import java.util.*
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.PackLocationInfo
import net.minecraft.server.packs.PackResources
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.metadata.MetadataSectionType
import net.minecraft.server.packs.repository.PackSource

class IslandUtilsPackResources : PackResources {
    override fun getRootResource(vararg strings: String) = null
    override fun getResource(packType: PackType, identifier: Identifier) = null
    override fun listResources(packType: PackType, string: String, string2: String, resourceOutput: PackResources.ResourceOutput) {}
    override fun getNamespaces(packType: PackType) = emptySet<String>()
    override fun <T : Any> getMetadataSection(metadataSectionType: MetadataSectionType<T>) = null
    override fun location() = PackLocationInfo(
        "island_utils_injector",
        Component.literal("IslandUtils Injected Sounds"),
        PackSource.BUILT_IN,
        Optional.empty()
    )
    override fun close() {}
}
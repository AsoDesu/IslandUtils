package dev.asodesu.islandutils.api.server

import dev.asodesu.islandutils.IslandUtils
import dev.asodesu.islandutils.api.extentions.calculateSha256
import dev.asodesu.islandutils.api.extentions.debug
import dev.asodesu.islandutils.api.extentions.minecraft
import net.minecraft.client.multiplayer.ServerData
import net.minecraft.network.protocol.game.ClientboundLoginPacket

object ServerSessionHandler {
    private val hostnames = listOf(
        "mccisland.net",
        "mccisland.com"
    )
    private val nonProdIps = listOf(
        "e927084bb931f83eece6780afd9046f121a798bf3ff3c78a9399b08c1dfb1aec", // bigrat.mccisland.net
        "0c932ffaa687c756c4616a24eb49389213519ea8d18e0d9bdfd2d335771c35c7",
        "7f0d15bbb2ffaee1bbf0d23e5746afb753333d590f71ff8a5a186d86c3e79dda",
        "09445264a9c515c83fc5a0159bda82e25d70d499f80df4a2d1c2f7e2ae6af997"
    ) // oooo top secret noxcrew secrets :3

    var isOnline = false
    var isProduction = true
    var joinTime = System.currentTimeMillis()

    // here we run this on the render thread cuz for some reason if you don't do that
    //  you can't join any minecraft servers :shrug:
    //
    //  i have since discovered that the "debug" function cannot be called on
    //  the non-render thread since it tries to put things in chat lol
    fun onConnect(serverData: ServerData) = minecraft.submit {
        if (isIslandServer(serverData)) {
            isOnline = true
            isProduction = isProductionServer(serverData)
            joinTime = System.currentTimeMillis()
            debug("Joined a ${if (isProduction) "production" else "non-production"} MCC Island server")
            ServerEvents.SERVER_JOIN.invoker().onServerConnect()
        } else {
            isOnline = false
            IslandUtils.logger.info("Joined a minecraft server (not mcc island, features disabled)")
        }
    }

    fun onInstanceJoin(clientboundLoginPacket: ClientboundLoginPacket) = minecraft.submit {
        ServerEvents.INSTANCE_JOIN.invoker().onInstanceSwitch()
    }

    // idk if we need to run on the render thread here but we'll do it for good measure
    fun onDisconnect() = minecraft.submit {
        if (!isOnline) {
            IslandUtils.logger.info("Disconnected from a minecraft server (not mcc island, disconnect not triggered)")
            return@submit
        }
        ServerEvents.SERVER_DISCONNECT.invoker().onServerDisconnect()
        debug("Disconnected an MCC Island server")
        isOnline = false
        isProduction = true
    }

    fun isIslandServer(serverData: ServerData): Boolean {
        val hostname = serverData.ip.lowercase()
        return hostnames.any { hostname.contains(it) }
    }

    fun isProductionServer(serverData: ServerData): Boolean {
        val hostnameHash = serverData.ip.lowercase().calculateSha256()
        return !nonProdIps.contains(hostnameHash)
    }
}
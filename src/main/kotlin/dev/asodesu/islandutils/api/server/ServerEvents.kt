package dev.asodesu.islandutils.api.server

import dev.asodesu.islandutils.api.events.arrayBackedEvent

object ServerEvents {
    val SERVER_JOIN = arrayBackedEvent { callbacks ->
        ServerJoinCallback {
            callbacks.forEach { it.onServerConnect() }
        }
    }

    val INSTANCE_JOIN = arrayBackedEvent { callbacks ->
        InstanceJoinCallback {
            callbacks.forEach { it.onInstanceSwitch() }
        }
    }

    val SERVER_DISCONNECT = arrayBackedEvent { callbacks ->
        ServerDisconnectCallback {
            callbacks.forEach { it.onServerDisconnect() }
        }
    }
}
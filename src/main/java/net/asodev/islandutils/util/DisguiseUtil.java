package net.asodev.islandutils.util;

import net.asodev.islandutils.IslandUtilsClient;
import net.asodev.islandutils.state.MccIslandState;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import net.minecraft.client.player.LocalPlayer;

import java.util.HashMap;
import java.util.UUID;

public class DisguiseUtil {
    private static HashMap<UUID, Long> disguiseCooldownMap = new HashMap<>();
    private static final Long disguiseCooldownTime = 1000L;
    public static void registerDisguiseInput() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(IslandUtilsClient.disguiseKeyBind.isDown() && client.player != null) {
                if(attemptUseDisguiseKey(client.player) && MccIslandState.isOnline()) {
                    client.player.connection.sendCommand("disguise");
                }
            }
        });
    }

    private static Boolean attemptUseDisguiseKey(LocalPlayer player) {
        if(!disguiseCooldownMap.containsKey(player.getUUID()) || System.currentTimeMillis() - disguiseCooldownMap.get(player.getUUID()) > disguiseCooldownTime) {
            disguiseCooldownMap.put(player.getUUID(), System.currentTimeMillis());
            return true;
        } else {
            return false;
        }
    }
}
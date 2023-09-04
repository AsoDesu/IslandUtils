package net.asodev.islandutils.util;

import net.asodev.islandutils.IslandUtilsClient;
import net.asodev.islandutils.state.MccIslandState;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import net.minecraft.client.player.LocalPlayer;

import java.util.HashMap;
import java.util.UUID;

public class DisguiseUtil {
    private static final Long disguiseCooldownTime = 1000L;
    private static long lastDisguiseUseTimestamp = -1;

    public static void registerDisguiseInput() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (IslandUtilsClient.disguiseKeyBind.isDown() && client.player != null) {
                if (attemptUseDisguiseKey() && MccIslandState.isOnline()) {
                    client.player.connection.sendCommand("disguise");
                }
            }
        });
    }

    private static Boolean attemptUseDisguiseKey() {
        long currentTimestamp = System.currentTimeMillis();
            if (currentTimestamp - lastDisguiseUseTimestamp < disguiseCooldownTime)
                return false;
        lastDisguiseUseTimestamp = currentTimestamp;
        return true;
    }
}
package net.asodev.islandutils.modules.plobby;

import net.asodev.islandutils.IslandUtilsClient;
import net.asodev.islandutils.state.MccIslandState;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class PlobbyKeybind {

    public static void registerKeybind() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!MccIslandState.isOnline()) return;
            if (!IslandUtilsClient.openPlobbyKey.consumeClick() || client.player == null) return;
            client.player.connection.sendCommand("plobby");
        });
    }

}

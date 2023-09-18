package net.asodev.islandutils.modules.plobby;

import net.asodev.islandutils.IslandUtilsClient;
import net.asodev.islandutils.IslandUtilsEvents;
import net.asodev.islandutils.state.MccIslandState;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

public class PlobbyFeatures {
    public static long lastCopy = 0;
    private static final Component copiedMessage = Component.literal("Copied code to clipboard!")
            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#ffff00")));

    public static void registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!MccIslandState.isOnline()) return;
            if (!IslandUtilsClient.openPlobbyKey.consumeClick() || client.player == null) return;
            client.player.connection.sendCommand("plobby"); // Do /plobby to open the plobby menu
        });

        IslandUtilsEvents.CHAT_MESSAGE.register((state, modify) -> {
            if ((System.currentTimeMillis() - lastCopy) > 5000) return; // If we copied the code less than 5s ago
            modify.replace(copiedMessage); // Replace with the copy success message
            lastCopy = 0; // Reset the copy time
        });
    }

}

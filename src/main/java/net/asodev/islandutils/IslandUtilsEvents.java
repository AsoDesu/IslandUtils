package net.asodev.islandutils;

import net.asodev.islandutils.state.GAME;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.Minecraft;

public class IslandUtilsEvents {

    public static Event<GameChangeCallback> GAME_CHANGE = EventFactory.createArrayBacked(GameChangeCallback.class, callbacks -> game -> {
        for (GameChangeCallback callback : callbacks) {
            callback.onGameChange(game);
        }
    });

    @FunctionalInterface
    public interface GameChangeCallback {
        void onGameChange(GAME to);
    }

}

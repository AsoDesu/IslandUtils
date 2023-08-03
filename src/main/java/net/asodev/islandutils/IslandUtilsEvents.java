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
    public static Event<GameUpdateCallback> GAME_UPDATE = EventFactory.createArrayBacked(GameUpdateCallback.class, callbacks -> game -> {
        for (GameUpdateCallback callback : callbacks) {
            callback.onGameUpdate(game);
        }
    });

    @FunctionalInterface
    public interface GameChangeCallback {
        void onGameChange(GAME to);
    }

    @FunctionalInterface
    public interface GameUpdateCallback {
        void onGameUpdate(GAME to);
    }

}

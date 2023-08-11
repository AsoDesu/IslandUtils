package net.asodev.islandutils;

import com.noxcrew.noxesium.network.clientbound.ClientboundMccGameStatePacket;
import net.asodev.islandutils.state.Game;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

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
    public static Event<GameStateChange> GAME_STATE_CHANGE = EventFactory.createArrayBacked(GameStateChange.class, callbacks -> state -> {
        for (GameStateChange callback : callbacks) {
            callback.onGameUpdate(state);
        }
    });

    @FunctionalInterface
    public interface GameChangeCallback {
        void onGameChange(Game to);
    }

    @FunctionalInterface
    public interface GameUpdateCallback {
        void onGameUpdate(Game to);
    }

    @FunctionalInterface
    public interface GameStateChange {
        void onGameUpdate(ClientboundMccGameStatePacket state);
    }

}

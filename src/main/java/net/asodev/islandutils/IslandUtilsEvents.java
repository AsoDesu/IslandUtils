package net.asodev.islandutils;

import com.noxcrew.noxesium.network.clientbound.ClientboundMccGameStatePacket;
import net.asodev.islandutils.state.Game;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.resources.server.PackLoadFeedback;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;
import java.util.function.Consumer;

public class IslandUtilsEvents {

    public static Event<PacketReceived> PACKET_RECEIVED = EventFactory.createArrayBacked(PacketReceived.class, callbacks -> (packet, callbackInfo) -> {
        for (PacketReceived callback : callbacks) {
            callback.onPacketReceived(packet, callbackInfo);
        }
    });

    public static Event<PackReportFinalResult> PACK_FINAL_RESULT = EventFactory.createArrayBacked(PackReportFinalResult.class, callbacks -> (uuid, finalResult) -> {
        for (PackReportFinalResult callback : callbacks) {
            callback.packReportFinalResult(uuid, finalResult);
        }
    });

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

    public static Event<ConnectionCallback> JOIN_MCCI = EventFactory.createArrayBacked(ConnectionCallback.class, callbacks -> () -> {
        for (ConnectionCallback callback : callbacks) {
            callback.onEvent();
        }
    });

    public static Event<ConnectionCallback> QUIT_MCCI = EventFactory.createArrayBacked(ConnectionCallback.class, callbacks -> () -> {
        for (ConnectionCallback callback : callbacks) {
            callback.onEvent();
        }
    });

    // GAME PACKET EVENTS
    public static Event<ChatMessageEvent> CHAT_MESSAGE = EventFactory.createArrayBacked(ChatMessageEvent.class, callbacks -> (state, modify) -> {
        for (ChatMessageEvent callback : callbacks) {
            callback.onChatMessage(state, modify);
        }
    });

    @FunctionalInterface
    public interface PacketReceived {
        void onPacketReceived(Packet<?> packet, CallbackInfo callbackInfo);
    }

    @FunctionalInterface
    public interface PackReportFinalResult {
        void packReportFinalResult(UUID uuid, PackLoadFeedback.FinalResult finalResult);
    }

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

    @FunctionalInterface
    public interface ConnectionCallback {
        void onEvent();
    }

    @FunctionalInterface
    public interface ChatMessageEvent {
        void onChatMessage(ClientboundSystemChatPacket state, Modifier<Component> modify);
    }

    public static class Modifier<T> {
        T replacement = null;
        boolean shouldCancel = false;

        public void cancel() {
            shouldCancel = true;
        }

        public void replace(T replacement) {
            this.replacement = replacement;
        }

        public void withCancel(Consumer<Boolean> consumer) {
            if (shouldCancel) consumer.accept(shouldCancel);
        }

        public void withReplacement(Consumer<T> consumer) {
            if (replacement != null) consumer.accept(replacement);
        }
    }

}

package net.asodev.islandutils.util;

import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundCommandSuggestionsPacket;
import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A class to handle the collection of command suggestions from the server.
 * <br>
 * @param <T> The type that the suggestions should be mapped to.
 */
public class SuggestionProvider<T> {

    private static final Int2ObjectMap<SuggestionProvider<?>> PROVIDERS = new Int2ObjectOpenHashMap<>();

    private static int nextTransactionId = 7345664; // random number to start with

    private final int transactionId;
    private final String command;
    private final Function<Suggestion, T> mapper;

    private final List<Consumer<List<T>>> listeners = new ArrayList<>();

    /**
     * @param command The command to request suggestions for.
     * @param mapper The function to map the suggestions to the desired type.
     */
    public SuggestionProvider(String command, Function<Suggestion, T> mapper) {
        this.transactionId = nextTransactionId;
        this.command = command;
        this.mapper = mapper;
        PROVIDERS.put(nextTransactionId, this);

        SuggestionProvider.nextTransactionId++;
    }

    public void send() {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection != null) {
            connection.send(new ServerboundCommandSuggestionPacket(this.transactionId, this.command));
        }
    }

    public SuggestionProvider<T> registerListener(Consumer<List<T>> listener) {
        listeners.add(listener);
        return this;
    }

    public SuggestionProvider<T> unregisterListener(Consumer<List<T>> listener) {
        listeners.remove(listener);
        return this;
    }

    protected void invoke(Suggestions suggestions) {
        List<T> mappedSuggestions = suggestions.getList().stream().map(mapper).collect(Collectors.toList());
        listeners.forEach(listener -> listener.accept(mappedSuggestions));
    }

    public static boolean invoke(ClientboundCommandSuggestionsPacket packet) {
        SuggestionProvider<?> provider = PROVIDERS.get(packet.getId());
        if (provider == null) return false;
        provider.invoke(packet.getSuggestions());
        return true;
    }

    public static SuggestionProvider<String> ofString(String command) {
        return new SuggestionProvider<>(command, Suggestion::getText);
    }

}

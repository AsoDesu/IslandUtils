package net.asodev.islandutils.modules;

import net.asodev.islandutils.IslandUtilsEvents;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.state.Game;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.util.ChatUtils;
import net.asodev.islandutils.util.FontUtils;
import net.asodev.islandutils.util.Scheduler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundCommandSuggestionsPacket;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FriendsInGame {

    private static final int TRANSACTION_ID = 6775161;
    private static final ServerboundCommandSuggestionPacket SERVERBOUND_COMMAND_SUGGESTION_PACKET = new ServerboundCommandSuggestionPacket(TRANSACTION_ID, "/friend remove ");
    private static final Pattern INSTANCE_PATTERN = Pattern.compile("(?i)(INSTANCE|FISHTANCE) (\\d+)", Pattern.UNICODE_CHARACTER_CLASS);
    private static final long DEBOUNCE_TIME_MS = 10;
    private static final int MESSAGE_SEND_DELAY_TICKS = 35;

    private static List<String> friends = new ArrayList<>();
    private static @Nullable Integer currentInstance = null;
    private static @Nullable Integer lastInstance = null;
    private static long lastFriendMessageTimestamp = 0;

    public static void init() {

        IslandUtilsEvents.GAME_UPDATE.register((game) -> {
            friends.clear();
            ClientPacketListener connection = Minecraft.getInstance().getConnection();
            if (connection != null) connection.send(SERVERBOUND_COMMAND_SUGGESTION_PACKET);
        });

        IslandUtilsEvents.PACKET_RECEIVED.register((packet, callbackInfo) -> {

            // For finding what instance (or fishtance…) the player is on
            if (packet instanceof ClientboundTabListPacket tablistPacket) {
                var rawString = tablistPacket.footer().getString();
                var matcher = INSTANCE_PATTERN.matcher(rawString);

                if (matcher.find()) {
                    var newInstance = Integer.parseInt(matcher.group(2));
                    lastInstance = currentInstance;
                    currentInstance = newInstance;
                } else {
                    lastInstance = null;
                    currentInstance = null;
                }
            }

            // Get friend names
            // Moved from PacketListenerMixin
            else if (packet instanceof ClientboundCommandSuggestionsPacket suggestionsPacket) {
                if (suggestionsPacket.id() != TRANSACTION_ID) return;

                callbackInfo.cancel();
                List<String> friends = suggestionsPacket
                        .suggestions()
                        .stream().map(ClientboundCommandSuggestionsPacket.Entry::text)
                        .collect(Collectors.toList());
                FriendsInGame.setFriends(friends);
            }
        });
    }

    public static void setFriends(List<String> friends) {
        FriendsInGame.friends = friends;
        Scheduler.schedule(MESSAGE_SEND_DELAY_TICKS, FriendsInGame::sendFriendsInGame);
    }

    public static void sendFriendsInGame(Minecraft client) {
        if (!shouldSendFriends()) return;

        var currentTime = System.currentTimeMillis();
        if (currentTime - lastFriendMessageTimestamp < DEBOUNCE_TIME_MS) return;
        lastFriendMessageTimestamp = currentTime;

        StringBuilder friendsInThisGame = new StringBuilder();
        boolean hasFriends = false;

        ClientPacketListener connection = client.getConnection();
        if (connection == null) return;
        for (PlayerInfo p : connection.getOnlinePlayers()) {
            String name = p.getProfile().getName();
            if (friends.contains(name)) {
                hasFriends = true;
                friendsInThisGame.append(", ").append(name);
            }
        }

        if (!hasFriends) return;
        String friendString = friendsInThisGame.toString().replaceFirst(", ", "");

        String text = I18n.get("islandutils.message.friends.thisGame");
        if (MccIslandState.getGame() == Game.HUB) text = I18n.get("islandutils.message.friends.thisLobby");

        Component component = Component.literal("[").withStyle(ChatFormatting.GREEN)
                .append(FontUtils.ICON_SOCIAL)
                .append(Component.literal("] " + text).withStyle(ChatFormatting.GREEN))
                .append(Component.literal(friendString).withStyle(ChatFormatting.YELLOW));
        ChatUtils.send(component);
    }

    public static boolean shouldSendFriends() {
        var game = MccIslandState.getGame();
        var misc = IslandOptions.getMisc();

        if (isInSameInstance(game)) {
            return false;
        }

        return game == Game.HUB
                ? misc.isShowFriendsInLobby()
                : misc.isShowFriendsInGame();
    }

    private static boolean isInSameInstance(Game game) {
        return (game == Game.HUB || game == Game.FISHING)
                && currentInstance != null
                && currentInstance.equals(lastInstance);
    }
}
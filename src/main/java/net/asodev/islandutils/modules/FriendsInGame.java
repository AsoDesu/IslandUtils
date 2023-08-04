package net.asodev.islandutils.modules;

import net.asodev.islandutils.IslandUtilsEvents;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.state.Game;
import net.asodev.islandutils.util.ChatUtils;
import net.asodev.islandutils.util.Scheduler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;

import java.util.ArrayList;
import java.util.List;

import static net.asodev.islandutils.util.ChatUtils.iconsFontStyle;

public class FriendsInGame {
    public static final int TRANSACTION_ID = 6775161;
    private static List<String> friends = new ArrayList<>();

    public static void init() {
        IslandUtilsEvents.GAME_CHANGE.register((game) -> {
            if (game == Game.HUB) return;

            friends.clear();
            ServerboundCommandSuggestionPacket packet = new ServerboundCommandSuggestionPacket(TRANSACTION_ID, "/friend remove ");
            ClientPacketListener connection = Minecraft.getInstance().getConnection();
            if (connection != null) connection.send(packet);
        });
    }

    public static List<String> getFriends() {
        return friends;
    }
    public static void setFriends(List<String> friends) {
        FriendsInGame.friends = friends;
        Scheduler.schedule(25, FriendsInGame::sendFriendsInGame);
    }

    public static void sendFriendsInGame(Minecraft client) {
        if (!IslandOptions.getMisc().isShowFriendsInGame()) return;
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
        String friendString = friendsInThisGame.toString().replaceFirst(", ","");

        Component component = Component.literal("[").withStyle(ChatFormatting.GREEN)
                .append(Component.literal("\ue001").withStyle(iconsFontStyle))
                .append(Component.literal("] Friends in this game: ").withStyle(ChatFormatting.GREEN))
                .append(Component.literal(friendString).withStyle(ChatFormatting.YELLOW));
        ChatUtils.send(component);
    }

}

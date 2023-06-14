package net.asodev.islandutils.state;

import net.asodev.islandutils.discord.DiscordPresenceUpdator;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.state.faction.FACTION;
import net.asodev.islandutils.util.ChatUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class MccIslandState {

    public static final int TRANSACTION_ID = 6775161;

    private static GAME game = GAME.HUB;
    private static String modifier = "INACTIVE";
    private static String map = "UNKNOWN";
    private static FACTION faction;
    private static List<String> friends = new ArrayList<>();

    public static String getModifier() {
        return modifier;
    }
    public static void setModifier(String modifier) {
        MccIslandState.modifier = modifier;
    }

    public static GAME getGame() {
        return game;
    }
    public static void setGame(GAME game) {
        if (MccIslandState.game != game && game != GAME.HUB) {
            friends.clear();
            ServerboundCommandSuggestionPacket packet = new ServerboundCommandSuggestionPacket(TRANSACTION_ID, "/friend remove ");
            ClientPacketListener connection = Minecraft.getInstance().getConnection();
            if (connection != null) connection.send(packet);
        }
        ChatUtils.debug("MccIslandState - Changed game to: " + game);
        MccIslandState.game = game;
    }

    public static void setMap(String map) {
        MccIslandState.map = map;
    }
    public static String getMap() {
        return map;
    }

    public static FACTION getFaction() {
        return faction;
    }
    public static void setFaction(FACTION faction) {
        //ChatUtils.debug("Detected Faction: " + faction);
        DiscordPresenceUpdator.setLevel(DiscordPresenceUpdator.lastLevel);
        MccIslandState.faction = faction;
    }

    public static List<String> getFriends() {
        return friends;
    }
    public static void setFriends(List<String> friends) {
        MccIslandState.friends = friends;

        if (!IslandOptions.getOptions().isShowFriendsInGame()) return;
        AtomicReference<String> friendsInThisGame = new AtomicReference<>("");
        AtomicBoolean hasFriends = new AtomicBoolean(false);

        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection == null) return;
        connection.getLevel().players().forEach(p -> {
            String name = p.getName().getString();
            if (friends.contains(name)) {
                hasFriends.set(true);
                friendsInThisGame.set(friendsInThisGame + ", " + name);
            }
        });
        friendsInThisGame.set(friendsInThisGame.get().replaceFirst(", ",""));
        if (!hasFriends.get()) return;

        Component component = Component.literal("[").withStyle(ChatFormatting.GREEN)
                .append(Component.literal("\ue001").withStyle(Style.EMPTY.withFont(new ResourceLocation("island","icons"))))
                .append(Component.literal("] Friends in this game: ").withStyle(ChatFormatting.GREEN))
                .append(Component.literal(friendsInThisGame.get()).withStyle(ChatFormatting.YELLOW));
        ChatUtils.send(component);
    }

    public static boolean isOnline() {
        ServerData currentServer = Minecraft.getInstance().getCurrentServer();
        return currentServer != null && currentServer.ip.toLowerCase().contains("mccisland.net");
    }
}

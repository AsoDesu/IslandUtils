package net.asodev.islandutils.state;

import net.asodev.islandutils.discord.DiscordPresenceUpdator;
import net.asodev.islandutils.mixins.accessors.TabListAccessor;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.state.faction.FACTION;
import net.asodev.islandutils.util.ChatUtils;
import net.asodev.islandutils.util.Scheduler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static net.asodev.islandutils.util.ChatUtils.iconsFontStyle;

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
        if (MccIslandState.game != game) {
            ChatUtils.debug("MccIslandState - Changed game to: " + game);

            if (game != GAME.HUB) {
                friends.clear();
                ServerboundCommandSuggestionPacket packet = new ServerboundCommandSuggestionPacket(TRANSACTION_ID, "/friend remove ");
                ClientPacketListener connection = Minecraft.getInstance().getConnection();
                if (connection != null) connection.send(packet);
            }
        }
        MccIslandState.game = game;
    }

    public static void updateGame(Component displayName, String tablistTitle) {
        String title = displayName.getString();

        // Check if the name of the game is not the aqua color, then we check if we are not in parkour warrior
        // Parkour Warrior (at least solo mode) is exception and has white name in the scoreboard title
        // if both checks are false, we are in hub!
        if (!isGameDisplayName(displayName) && !title.contains("PARKOUR WARRIOR")) {
            MccIslandState.setGame(GAME.HUB);
        } else { // We're in a game!!!
            // These checks are pretty self-explanatory
            if (title.contains("HOLE IN THE WALL")) {
                MccIslandState.setGame(GAME.HITW);
            } else if (title.contains("TGTTOS")) {
                MccIslandState.setGame(GAME.TGTTOS);
            } else if (title.contains("SKY BATTLE")) {
                MccIslandState.setGame(GAME.SKY_BATTLE);
            } else if (title.contains("BATTLE BOX")) {
                MccIslandState.setGame(GAME.BATTLE_BOX);
            } else if (title.contains("PARKOUR WARRIOR")) {
                if (tablistTitle.contains("PARKOUR WARRIOR SURVIVOR")) {
                    MccIslandState.setGame(GAME.PARKOUR_WARRIOR_SURVIVOR);
                } else if (tablistTitle.contains("Parkour Warrior - ")) {
                    MccIslandState.setGame(GAME.PARKOUR_WARRIOR_DOJO);
                } else {
                    MccIslandState.setGame(GAME.HUB);
                }
            } else {
                MccIslandState.setGame(GAME.HUB); // Somehow we're in a game, but not soooo hub it is!!
            }
        }
        DiscordPresenceUpdator.updatePlace(); // Update where we are on discord presence
    }
    static TextColor aqua = TextColor.fromLegacyFormat(ChatFormatting.AQUA);
    private static boolean isGameDisplayName(Component component) {
        for (Component sibling : component.getSiblings()) { // Get all the elements of this component
            if (sibling.getStyle().getColor() == aqua) return true; // If it's aqua, YES
        }
        return false; // If not... no :(
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
        Scheduler.schedule(20, MccIslandState::sendFriendsInGame);
    }

    public static void sendFriendsInGame(Minecraft client) {
        if (!IslandOptions.getMisc().isShowFriendsInGame()) return;
        StringBuilder friendsInThisGame = new StringBuilder();
        boolean hasFriends = false;

        ClientPacketListener connection = client.getConnection();
        if (connection == null) return;
        for (Player p : connection.getLevel().players()) {
            String name = p.getName().getString();
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

    public static boolean isOnline() {
        ServerData currentServer = Minecraft.getInstance().getCurrentServer();
        return currentServer != null && currentServer.ip.toLowerCase().contains("mccisland.net");
    }
}

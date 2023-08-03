package net.asodev.islandutils.state;

import net.asodev.islandutils.IslandUtilsEvents;
import net.asodev.islandutils.discord.DiscordPresenceUpdator;
import net.asodev.islandutils.mixins.accessors.TabListAccessor;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.state.faction.FACTION;
import net.asodev.islandutils.util.ChatUtils;
import net.asodev.islandutils.util.Scheduler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
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

    private static GAME game = GAME.HUB;
    private static String modifier = "INACTIVE";
    private static String map = "UNKNOWN";
    private static FACTION faction;

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
            IslandUtilsEvents.GAME_CHANGE.invoker().onGameChange(game);
        }
        MccIslandState.game = game;
    }

    public static void updateGame(Component displayName, String tablistTitle) {
        String title = displayName.getString();

        // Check for PKW Tablist Titles
        if (tablistTitle.contains("PARKOUR WARRIOR SURVIVOR")) {
            MccIslandState.setGame(GAME.PARKOUR_WARRIOR_SURVIVOR);
            return;
        } else if (tablistTitle.contains("Parkour Warrior - ")) {
            MccIslandState.setGame(GAME.PARKOUR_WARRIOR_DOJO);
            return;
        }

        if (!isGameDisplayName(displayName)) {
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
            } else {
                MccIslandState.setGame(GAME.HUB); // Somehow we're in a game, but not soooo hub it is!!
            }
        }
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

    public static boolean isOnline() {
        ServerData currentServer = Minecraft.getInstance().getCurrentServer();
        return currentServer != null && currentServer.ip.toLowerCase().contains("mccisland.net");
    }
}

package net.asodev.islandutils.state;

import net.asodev.islandutils.discord.DiscordPresenceUpdator;
import net.asodev.islandutils.state.faction.FACTION;
import net.asodev.islandutils.util.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;

public class MccIslandState {

    private static STATE game = STATE.HUB;
    private static String modifier = "INACTIVE";
    private static String map = "UNKNOWN";
    private static FACTION faction;

    public static STATE getGame() {
        return game;
    }

    public static String getModifier() {
        return modifier;
    }

    public static void setGame(STATE game) {
        MccIslandState.game = game;
    }

    public static void setModifier(String modifier) {
        MccIslandState.modifier = modifier;
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
        ChatUtils.debug("Detected Faction: " + faction);
        DiscordPresenceUpdator.setLevel(DiscordPresenceUpdator.lastLevel);
        MccIslandState.faction = faction;
    }

    public static boolean isOnline() {
        ServerData currentServer = Minecraft.getInstance().getCurrentServer();
        return currentServer != null && currentServer.ip.toLowerCase().contains("mccisland.net");
    }
}

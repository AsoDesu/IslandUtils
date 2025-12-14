package net.asodev.islandutils.state;

import net.asodev.islandutils.IslandUtilsEvents;
import net.asodev.islandutils.util.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;

public class MccIslandState {

    private static Game game = Game.HUB;
    private static String modifier = "INACTIVE";
    private static String map = "UNKNOWN";
    private static String subType = "";

    public static String getModifier() {
        return modifier;
    }
    public static void setModifier(String modifier) {
        MccIslandState.modifier = modifier;
    }

    public static Game getGame() {
        return game;
    }

    public static void setGame(Game game) {
        if (MccIslandState.game != game) {
            ChatUtils.debug("MccIslandState - Changed game to: " + game);
            IslandUtilsEvents.GAME_CHANGE.invoker().onGameChange(game);
        }
        MccIslandState.game = game;
        IslandUtilsEvents.GAME_UPDATE.invoker().onGameUpdate(game);
    }

    public static void setMap(String map) {
        MccIslandState.map = map;
    }
    public static String getMap() {
        return map;
    }

    public static String getSubType() {
        return subType;
    }
    public static void setSubType(String subType) {
        MccIslandState.subType = subType;
    }

    public static boolean isOnline() {
        ServerData currentServer = Minecraft.getInstance().getCurrentServer();
        if (currentServer == null) return false;
        String ip = currentServer.ip.toLowerCase();
        return ip.contains("mccisland.net") || ip.contains("mccisland.com");
    }
}

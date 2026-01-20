package net.asodev.islandutils.state;

import net.asodev.islandutils.IslandUtilsEvents;
import net.asodev.islandutils.util.ChatUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;

import javax.annotation.Nullable;
import java.util.List;

public class MccIslandState {

    private static Game game = Game.HUB;
    private static String modifier = "INACTIVE";
    private static String map = "UNKNOWN";
    private static List<String> types = List.of();

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

    public static @Nullable String getFishingIsland() {
        if (game != Game.FISHING) return null;
        return types.get(2);
    }

    public static void updateGame(Component displayName, String tablistTitle) {
        String title = displayName.getString();

        // Check for PKW Tablist Titles
        if (tablistTitle.contains("PARKOUR WARRIOR SURVIVOR")) {
            MccIslandState.setGame(Game.PARKOUR_WARRIOR_SURVIVOR);
            return;
        } else if (tablistTitle.contains("Parkour Warrior - ")) {
            MccIslandState.setGame(Game.PARKOUR_WARRIOR_DOJO);
            return;
        }

        if (!isGameDisplayName(displayName)) {
            MccIslandState.setGame(Game.HUB);
        } else { // We're in a game!!!
            // These checks are pretty self-explanatory
            if (title.contains("HOLE IN THE WALL")) {
                MccIslandState.setGame(Game.HITW);
            } else if (title.contains("TGTTOS")) {
                MccIslandState.setGame(Game.TGTTOS);
            } else if (title.contains("SKY BATTLE")) {
                MccIslandState.setGame(Game.SKY_BATTLE);
            } else if (title.contains("BATTLE BOX")) {
                MccIslandState.setGame(Game.BATTLE_BOX);
            } else {
                MccIslandState.setGame(Game.HUB); // Somehow we're in a game, but not soooo hub it is!!
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

    public static List<String> getTypes() {
        return types;
    }
    public static void setTypes(List<String> types) {
        MccIslandState.types = types;
    }

    public static boolean isOnline() {
        ServerData currentServer = Minecraft.getInstance().getCurrentServer();
        if (currentServer == null) return false;
        String ip = currentServer.ip.toLowerCase();
        return ip.contains("mccisland.net") || ip.contains("mccisland.com");
    }
}

package net.asodev.islandutils.discord;

import net.asodev.islandutils.IslandUtilsEvents;
import net.asodev.islandutils.state.Game;
import net.asodev.islandutils.state.MccIslandState;
import net.minecraft.client.resources.language.I18n;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.asodev.islandutils.discord.DiscordPresenceUpdator.activity;

public class FishingPresenceUpdator {
    public static List<String> temperatures = List.of("temperate", "tropical", "barren");
    public static Map<String, String> islandNames = new HashMap<>();

    static {
        islandNames.put("temperate_1", "Verdant Woods");
        islandNames.put("temperate_2", "Floral Forest");
        islandNames.put("temperate_3", "Dark Grove");
        islandNames.put("temperate_grotto", "Sunken Swamp");

        islandNames.put("tropical_1", "Tropical Overgrowth");
        islandNames.put("tropical_2", "Coral Shores");
        islandNames.put("tropical_3", "Twisted Swamp");
        islandNames.put("tropical_grotto", "Mirrored Oasis");

        islandNames.put("barren_1", "Ancient Sands");
        islandNames.put("barren_2", "Blazing Canyon");
        islandNames.put("barren_3", "Ashen Wastes");
        islandNames.put("barren_grotto", "Volcanic Springs");
    }

    public static void init() {
    }

    public static void updateFishingPlace() {
        if (activity == null) return;

        String place = islandNames.get(MccIslandState.getSubType());
        if (place != null) {
            activity.setDetails(I18n.get("islandutils.discordPresence.details.inThePlace",place));
            activity.assets().setLargeImage(MccIslandState.getSubType().toLowerCase());
            activity.assets().setLargeText(place);

            activity.assets().setSmallImage("fishing");
            activity.assets().setSmallText(I18n.get("islandutils.discordPresence.smallText.fishing"));
        } else {
            activity.assets().setLargeImage("fishing");
            activity.assets().setLargeText(I18n.get("islandutils.discordPresence.largeText.fishing"));
        }
    }
}

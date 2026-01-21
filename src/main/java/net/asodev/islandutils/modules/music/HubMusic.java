package net.asodev.islandutils.modules.music;

import net.minecraft.resources.Identifier;

public class HubMusic {
    public static Identifier HUB_CLASSIC = get("hub_classic");
    public static Identifier HUB_LIKE = get("hub_like");
    public static Identifier HUBBIN = get("hubbin");
    public static Identifier ISLAND = get("island");
    public static Identifier OUR_HUB = get("our_hub");
    public static Identifier RELAX_HUB = get("relax_hub");
    public static Identifier WE_ARE = get("we_are");

    private static Identifier get(String name) {
        return Identifier.fromNamespaceAndPath("island", "island.music." + name);
    }
}

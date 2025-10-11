package net.asodev.islandutils.modules.music;

import net.minecraft.resources.ResourceLocation;

public class HubMusic {
    public static ResourceLocation HUB_CLASSIC = get("hub_classic");
    public static ResourceLocation HUB_LIKE = get("hub_like");
    public static ResourceLocation HUBBIN = get("hubbin");
    public static ResourceLocation ISLAND = get("island");
    public static ResourceLocation OUR_HUB = get("our_hub");
    public static ResourceLocation RELAX_HUB = get("relax_hub");
    public static ResourceLocation WE_ARE = get("we_are");

    private static ResourceLocation get(String name) {
        return ResourceLocation.fromNamespaceAndPath("island", "island.music." + name);
    }
}

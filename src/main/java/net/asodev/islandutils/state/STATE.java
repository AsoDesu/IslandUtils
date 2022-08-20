package net.asodev.islandutils.state;

import net.minecraft.resources.ResourceLocation;

public enum STATE {

    HUB("Hub", null),

    TGTTOS("To Get To The Other Side", new ResourceLocation("island", "island.music.tgttos")),
    HITW("Hole in the Wall", new ResourceLocation("island", "island.music.hitw")),
    BATTLE_BOX("Battle Box", new ResourceLocation("island", "island.music.battle_box")),
    SKY_BATTLE("Sky Battle", new ResourceLocation("island", "island.music.sky_battle"));

    final private String name;
    final private ResourceLocation location;
    STATE(String name, ResourceLocation location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public ResourceLocation getLocation() {
        return location;
    }
}

package net.asodev.islandutils.state;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public enum STATE {

    HUB("Hub", null),

    TGTTOS("TGTTOS", new ResourceLocation("island", "island.music.tgttos")),
    HITW("Hole in the Wall", new ResourceLocation("island", "island.music.hitw")),
    BATTLE_BOX("Battle Box", new ResourceLocation("island", "island.music.battle_box")),
    SKY_BATTLE("Sky Battle", new ResourceLocation("island", "island.music.sky_battle"));

    final private String name;
    final private ResourceLocation musicLocation;
    STATE(String name, ResourceLocation location) {
        this.name = name;
        this.musicLocation = location;
    }

    public String getName() {
        return name;
    }
    public ResourceLocation getMusicLocation() {
        return musicLocation;
    }

    @Nullable
    public static STATE getFromName(String name) {
        for (STATE value : STATE.values()) {
            if (name.contains(value.getName())) return value;
        }
        return null;
    }
}

package net.asodev.islandutils.state;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public enum Game {

    HUB("Hub", null),

    TGTTOS("TGTTOS", new ResourceLocation("island", "island.music.tgttos")),
    HITW("Hole in the Wall", new ResourceLocation("island", "island.music.hitw")),
    BATTLE_BOX("Battle Box", new ResourceLocation("island", "island.music.battle_box")),
    PARKOUR_WARRIOR_DOJO("Parkour Warrior Dojo", new ResourceLocation("island", "island.music.parkour_warrior")),
    PARKOUR_WARRIOR_SURVIVOR("Parkour Warrior Survivor", new ResourceLocation("island", "island.music.parkour_warrior")),
    SKY_BATTLE("Sky Battle", new ResourceLocation("island", "island.music.sky_battle"));

    final private String name;
    final private ResourceLocation musicLocation;
    Game(String name, ResourceLocation location) {
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
    public static Game getFromName(String name) {
        for (Game value : Game.values()) {
            if (name.contains(value.getName())) return value;
        }
        return null;
    }
}

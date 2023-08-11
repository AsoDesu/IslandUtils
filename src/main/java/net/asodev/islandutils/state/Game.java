package net.asodev.islandutils.state;

import com.noxcrew.noxesium.network.clientbound.ClientboundMccServerPacket;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

public enum Game {

    HUB("Hub", "", null),

    TGTTOS("TGTTOS", "tgttos", getMusicLocation("tgttos")),
    HITW("Hole in the Wall", "hole_in_the_wall", getMusicLocation("hitw")),
    BATTLE_BOX("Battle Box", "battle_box", getMusicLocation("battle_box")),
    PARKOUR_WARRIOR_SURVIVOR("Parkour Warrior Survivor", "parkour_warrior", "survival", getMusicLocation("parkour_warrior")),
    PARKOUR_WARRIOR_DOJO("Parkour Warrior Dojo", "parkour_warrior", getMusicLocation("parkour_warrior")),
    SKY_BATTLE_WATERFIGHT("Sky Battle: Water Fight", "sky_battle", "summer-remix", getMusicLocation("sky_battle")),
    SKY_BATTLE("Sky Battle", "sky_battle", getMusicLocation("sky_battle"));

    final private String name;
    final private String islandId;
    final private String subType;
    final private ResourceLocation musicLocation;
    Game(String name, String islandId, ResourceLocation location) {
        this.name = name;
        this.islandId = islandId;
        this.subType = null;
        this.musicLocation = location;
    }
    Game(String name, String islandId, String subType, ResourceLocation location) {
        this.name = name;
        this.islandId = islandId;
        this.subType = subType;
        this.musicLocation = location;
    }

    public String getName() {
        return name;
    }
    public ResourceLocation getMusicLocation() {
        return musicLocation;
    }

    public static ResourceLocation getMusicLocation(String name) {
        return new ResourceLocation("island", "island.music." + name);
    }

    public static Game fromPacket(ClientboundMccServerPacket packet) throws NoSuchElementException {
        for (Game game : values()) {
            if (game.islandId.equals(packet.associatedGame)) {
                if (game.subType != null && !game.subType.equals(packet.subType))
                    continue;
                return game;
            }
        }
        throw new NoSuchElementException("Game could not be found from '" + packet.associatedGame + "' (" + packet.subType + ")");
    }
}

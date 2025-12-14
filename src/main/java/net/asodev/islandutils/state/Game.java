package net.asodev.islandutils.state;

import com.noxcrew.noxesium.network.clientbound.ClientboundMccServerPacket;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

public enum Game {

    HUB("Hub", ""),
    FISHING("Fishing", ""),

    TGTTOS("TGTTOS", "tgttos", getMusicLocation("tgttos")),
    HITW("Hole in the Wall", "hole_in_the_wall", getMusicLocation("hitw")),
    BATTLE_BOX("Battle Box", "battle_box", getMusicLocation("battle_box"), true),
    PARKOUR_WARRIOR_SURVIVOR("Parkour Warrior Survivor", "parkour_warrior", "survival", getMusicLocation("parkour_warrior")),
    PARKOUR_WARRIOR_DOJO("Parkour Warrior Dojo", "parkour_warrior", getMusicLocation("parkour_warrior")),
    DYNABALL("Dynaball", "dynaball", getMusicLocation("dynaball"), true),
    ROCKET_SPLEEF_RUSH("Rocket Spleef Rush", "rocket_spleef", getMusicLocation("rsr")),
    SKY_BATTLE("Sky Battle", "sky_battle", getMusicLocation("sky_battle"), true);

    private final @NotNull String name;
    private final @Nullable String islandId;
    private final @Nullable String subType;
    private final @Nullable ResourceLocation musicLocation;
    private final boolean hasTeamChat;

    Game(@NotNull String name, @Nullable String islandId) {
        this(name, islandId, null, null, false);
    }
    Game(@NotNull String name, @Nullable String islandId, @Nullable ResourceLocation musicLocation) {
        this(name, islandId, null, musicLocation, false);
    }
    Game(@NotNull String name, @Nullable String islandId, @Nullable ResourceLocation musicLocation, boolean hasTeamChat) {
        this(name, islandId, null, musicLocation, hasTeamChat);
    }
    Game(@NotNull String name, @Nullable String islandId, @Nullable String subType, @Nullable ResourceLocation musicLocation) {
        this(name, islandId, subType, musicLocation, false);
    }
    Game(@NotNull String name, @Nullable String islandId, @Nullable String subType, @Nullable ResourceLocation musicLocation, boolean hasTeamChat) {
        this.name = name;
        this.islandId = islandId;
        this.subType = subType;
        this.musicLocation = musicLocation;
        this.hasTeamChat = hasTeamChat;
    }

    public @NotNull String getName() {
        return name;
    }
    public @Nullable ResourceLocation getMusicLocation() {
        return musicLocation;
    }
    public boolean hasTeamChat() {
        return hasTeamChat;
    }

    public static ResourceLocation getMusicLocation(String name) {
        return ResourceLocation.fromNamespaceAndPath("island", "island.music." + name);
    }

    public static Game fromPacket(ClientboundMccServerPacket packet) throws NoSuchElementException {
        System.out.println("ServerType: " + packet.serverType() + ", AssociatedGame: " + packet.associatedGame() + ", SubType: " + packet.subType());

        if (packet.serverType().equals("lobby")) {
            return HUB;
        } else if (packet.serverType().equals("fishing")) {
            return FISHING;
        }

        for (Game game : values()) {
            if (game.islandId != null && game.islandId.equals(packet.associatedGame())) {
                if (game.subType != null && !game.subType.equals(packet.subType()))
                    continue;
                return game;
            }
        }
        throw new NoSuchElementException("Game could not be found from '" + packet.associatedGame() + "' (" + packet.subType() + ")");
    }
}

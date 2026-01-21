package net.asodev.islandutils.state;

import com.noxcrew.noxesium.core.mcc.ClientboundMccServerPacket;
import net.minecraft.resources.Identifier;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;

public enum Game {

    HUB("Hub", null, null),
    FISHING("Hub", null, null),

    TGTTOS("TGTTOS", List.of("tgttos", "solo"), getMusicLocation("tgttos")),
    HITW("Hole in the Wall", List.of("hole_in_the_wall", "solo"), getMusicLocation("hitw")),
    BATTLE_BOX("Battle Box", List.of("battle_box", "team"), getMusicLocation("battle_box"), true),
    BATTLE_BOX_ARENA("Battle Box Arena", List.of("battle_box", "team", "arena"), getMusicLocation("battle_box"), true),
    PARKOUR_WARRIOR_SURVIVOR("Parkour Warrior Survivor", List.of("parkour_warrior", "solo", "survival"), getMusicLocation("parkour_warrior")),
    PARKOUR_WARRIOR_DOJO("Parkour Warrior Dojo", null, getMusicLocation("parkour_warrior")),
    DYNABALL("Dynaball", List.of("dynaball", "team"), getMusicLocation("dynaball"), true),
    ROCKET_SPLEEF_RUSH("Rocket Spleef Rush", List.of("rocket_spleef", "solo"), getMusicLocation("rsr")),
    SKY_BATTLE("Sky Battle", List.of("sky_battle", "team", "quad"), getMusicLocation("sky_battle"), true);

    final private String name;
    final private @Nullable List<String> islandTypes;
    final private Identifier musicLocation;
    private boolean hasTeamChat = false;

    Game(String name, @Nullable List<String> islandTypes, Identifier location) {
        this.name = name;
        this.islandTypes = islandTypes;
        this.musicLocation = location;
    }
    Game(String name, @Nullable List<String> islandTypes, Identifier location, boolean hasTeamChat) {
        this(name, islandTypes, location);
        this.hasTeamChat = hasTeamChat;
    }

    public String getName() {
        return name;
    }
    public Identifier getMusicLocation() {
        return musicLocation;
    }
    public boolean hasTeamChat() {
        return hasTeamChat;
    }
    public boolean isHubGame() {
        return this == FISHING || this == HUB;
    }

    public static Identifier getMusicLocation(String name) {
        return Identifier.fromNamespaceAndPath("island", "island.music." + name);
    }

    public static Game fromPacket(ClientboundMccServerPacket packet) throws NoSuchElementException {
        switch (packet.server()) {
            case "fishing" -> {
                return FISHING;
            }
            case "dojo" -> {
                return PARKOUR_WARRIOR_DOJO;
            }
            case "lobby" -> {
                return HUB;
            }
        }

        for (Game game : values()) {
            if (game.islandTypes == null) continue;
            if (new HashSet<>(packet.types()).containsAll(game.islandTypes)) {
                // Handle an edge case for the Battle Box arena
                if ((game == BATTLE_BOX || game == BATTLE_BOX_ARENA)) {
                    if (packet.types().contains("arena")) return BATTLE_BOX_ARENA;
                    return BATTLE_BOX;
                }
                return game;
            }
        }
        throw new NoSuchElementException("Game could not be found from '" + packet.server() + "' (" + packet.types().toString() + ")");
    }
}

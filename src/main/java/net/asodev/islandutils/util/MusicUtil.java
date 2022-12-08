package net.asodev.islandutils.util;

import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.options.IslandSoundCategories;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.state.STATE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.protocol.game.ClientboundCustomSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

import java.util.Objects;

import static net.asodev.islandutils.options.IslandOptions.getOptions;

public class MusicUtil {

    public static void startMusic(ClientboundCustomSoundPacket clientboundCustomSoundPacket) {
        IslandOptions options = getOptions();
        switch (MccIslandState.getGame()) {
            case HITW -> { if (!options.isHitwMusic()) return; }
            case TGTTOS -> { if (!options.isTgttosMusic()) return; }
            case BATTLE_BOX -> { if (!options.isBbMusic()) return; }
            case SKY_BATTLE -> { if (!options.isSbMusic()) return; }
        }

        ResourceLocation location = MccIslandState.getGame().getMusicLocation();
        if (location == null) return;

        ChatUtils.debug("[MusicUtil] Starting: " + MccIslandState.getGame().name());
        stopMusic();

        float pitch = 1f;
        if (options.isTgttosDoubleTime() &&
            MccIslandState.getGame() == STATE.TGTTOS &&
            Objects.equals(MccIslandState.getModifier(), "DOUBLE TIME")) {
                pitch = 1.2f;
                ChatUtils.debug("[MusicUtil] Double Time on TGTTOS active! (Pitch: %s)", pitch);
        }

        SoundInstance instance = new SimpleSoundInstance(
                location,
                IslandSoundCategories.GAME_MUSIC,
                1,
                pitch,
                RandomSource.create(clientboundCustomSoundPacket.getSeed()),
                false,
                0,
                SoundInstance.Attenuation.LINEAR,
                clientboundCustomSoundPacket.getX(),
                clientboundCustomSoundPacket.getY(),
                clientboundCustomSoundPacket.getZ(),
                false);

        Minecraft.getInstance().getSoundManager().play(instance);
    }

    public static SoundInstance createSoundInstance(ClientboundCustomSoundPacket clientboundCustomSoundPacket, SoundSource source) {
        return new SimpleSoundInstance(
                clientboundCustomSoundPacket.getName(),
                source,
                clientboundCustomSoundPacket.getVolume(),
                clientboundCustomSoundPacket.getPitch(),
                RandomSource.create(clientboundCustomSoundPacket.getSeed()),
                false,
                0,
                SoundInstance.Attenuation.LINEAR,
                clientboundCustomSoundPacket.getX(),
                clientboundCustomSoundPacket.getY(),
                clientboundCustomSoundPacket.getZ(),
                false);
    }

    public static void stopMusic() {
        ResourceLocation location = MccIslandState.getGame().getMusicLocation();
        if (location == null) return;

        ChatUtils.debug("[MusicUtil] Stopping: " + location.getPath());
        Minecraft.getInstance().getSoundManager().stop(location, IslandSoundCategories.GAME_MUSIC);
    }

}

package net.asodev.islandutils.util;

import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.state.MccIslandState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.protocol.game.ClientboundCustomSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

import static net.asodev.islandutils.options.IslandOptions.getOptions;

public class MusicUtil {
    public static ResourceLocation lastPlayed;

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

        if (lastPlayed != null) {
            Minecraft.getInstance().getSoundManager().stop(lastPlayed, SoundSource.RECORDS);
        }

        SoundInstance instance = new SimpleSoundInstance(
                location,
                SoundSource.RECORDS,
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

        Minecraft.getInstance().getSoundManager().play(instance);
    }

    public static void stopMusic() {
        ResourceLocation location = MccIslandState.getGame().getMusicLocation();
        if (location == null) return;

        Minecraft.getInstance().getSoundManager().stop(location, SoundSource.RECORDS);
    }

}

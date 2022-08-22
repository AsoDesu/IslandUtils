package net.asodev.islandutils.util;

import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.state.STATE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.network.protocol.game.ClientboundCustomSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

public class MusicUtil {

    public static void startMusic(ClientboundCustomSoundPacket clientboundCustomSoundPacket) {
        ResourceLocation location = MccIslandState.getGame().getLocation();
        if (location == null) return;

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
        ResourceLocation location = MccIslandState.getGame().getLocation();
        if (location == null) return;

        Minecraft.getInstance().getSoundManager().stop(location, SoundSource.RECORDS);
    }

}

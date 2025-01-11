package net.asodev.islandutils.modules.music;

import net.asodev.islandutils.util.MCCSoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public record SoundInfo(ResourceLocation path, SoundSource category, double x, double y, double z, float volume, float pitch, long seed, boolean looping) {

    public SoundInfo withPath(ResourceLocation path) {
        return new SoundInfo(path, category, x, y, z, volume, pitch, seed, looping);
    }

    public SoundInfo withCategory(SoundSource category) {
        return new SoundInfo(path, category, x, y, z, volume, pitch, seed, looping);
    }

    public SoundInfo withPitch(float pitch) {
        return new SoundInfo(path, category, x, y, z, volume, pitch, seed, looping);
    }

    public SoundInfo withLooping(boolean looping) {
        return new SoundInfo(path, category, x, y, z, volume, pitch, seed, looping);
    }

    public static SoundInfo fromLocation(ResourceLocation location) {
        return new SoundInfo(location, SoundSource.MASTER, 0.0, 0.0, 0.0, 1f, 1f, 0L, false);
    }
    public static SoundInfo fromPacket(ClientboundSoundPacket soundPacket) {
        return new SoundInfo(
                soundPacket.getSound().value().getLocation(),
                soundPacket.getSource(),
                soundPacket.getX(),
                soundPacket.getY(),
                soundPacket.getZ(),
                soundPacket.getVolume(),
                soundPacket.getPitch(),
                soundPacket.getSeed(),
                false
        );
    }

    public MCCSoundInstance toSoundInstance() {
        return new MCCSoundInstance(this);
    }

}

package net.asodev.islandutils.modules.music;

import net.asodev.islandutils.util.MCCSoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public record SoundInfo(Identifier path, SoundSource category, double x, double y, double z, float volume, float pitch, long seed, boolean looping) {

    public SoundInfo withPath(Identifier path) {
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

    public static SoundInfo fromLocation(Identifier location) {
        return new SoundInfo(location, SoundSource.MASTER, 0.0, 0.0, 0.0, 1f, 1f, 0L, false);
    }
    public static SoundInfo fromPacket(ClientboundSoundPacket soundPacket) {
        return new SoundInfo(
                soundPacket.getSound().value().location(),
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

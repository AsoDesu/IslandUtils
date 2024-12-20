package net.asodev.islandutils.util;

import net.asodev.islandutils.modules.music.SoundInfo;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

import java.util.List;

public class MCCSoundInstance extends AbstractTickableSoundInstance {

    public ResourceLocation location;

    public float totalVolume;
    public float totalFadeTicks = 20f;
    public float fadeTicks = 0f;
    public boolean isFading = false;

    public MCCSoundInstance(ResourceLocation location, SoundSource soundSource, float volume,float pitch, RandomSource randomSource, double x, double y, double z) {
        super(SoundEvent.createVariableRangeEvent(location), soundSource, randomSource);
        this.location = location;
        this.volume = volume;
        this.totalVolume = volume;
        this.pitch = pitch;
        this.x = x;
        this.y = y;
        this.z = z;
        this.looping = false;
        this.delay = 0;
        this.attenuation = Attenuation.NONE;
        this.relative = false;
    }

    public MCCSoundInstance(SoundInfo soundInfo) {
        this(
                soundInfo.path(),
                soundInfo.category(),
                soundInfo.volume(),
                soundInfo.pitch(),
                RandomSource.create(soundInfo.seed()),
                soundInfo.x(),
                soundInfo.y(),
                soundInfo.z()
        );
        this.looping = soundInfo.looping();
    }

    public void fade(float ticks) {
        isFading = true;
        totalFadeTicks = ticks;
        fadeTicks = totalFadeTicks;
    }
    public void stopFwd() {
        stop();
    }

    @Override
    public void tick() {
        if (!isFading) return;
        if (fadeTicks <= 0) {
            this.volume = totalVolume * (fadeTicks/totalFadeTicks);
            this.stop();
        } else {
            this.volume = totalVolume * (fadeTicks/totalFadeTicks);
            fadeTicks -= 1;
        }
    }

    @Override
    public String toString() {
        return "MCCSoundInstance{" +
                "location=" + location +
                '}';
    }
}

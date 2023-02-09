package net.asodev.islandutils.util;

import net.minecraft.client.resources.sounds.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

public class MCCSoundInstance extends AbstractTickableSoundInstance {

    public float totalVolume;
    public float totalFadeTicks = 20f;
    public float fadeTicks = 0f;
    public boolean isFading = false;

    protected MCCSoundInstance(SoundEvent event, SoundSource soundSource, float f,float g, RandomSource randomSource, boolean bl, int i, SoundInstance.Attenuation attenuation, double d, double e, double h, boolean bl2) {
        super(event, soundSource, randomSource);
        this.volume = f;
        totalVolume = f;
        this.pitch = g;
        this.x = d;
        this.y = e;
        this.z = h;
        this.looping = bl;
        this.delay = i;
        this.attenuation = attenuation;
        this.relative = bl2;
    }

    public void fade(float ticks) {
        isFading = true;
        totalFadeTicks = ticks;
        fadeTicks = totalFadeTicks;
    }

    @Override
    public void tick() {
        if (!isFading) return;
        if (fadeTicks <= 0) this.stop();

        this.volume = totalVolume * (fadeTicks/totalFadeTicks);
        fadeTicks -= 1;
    }

    @Override
    public String toString() {
        return "MCCSoundInstance{" +
                "location=" + location +
                '}';
    }
}

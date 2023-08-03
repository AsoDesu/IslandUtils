package net.asodev.islandutils.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

import java.util.List;

public class MCCSoundInstance extends AbstractTickableSoundInstance {

    public static ResourceLocation location;

    public float totalVolume;
    public float totalFadeTicks = 20f;
    public float fadeTicks = 0f;
    public boolean isFading = false;

    protected MCCSoundInstance(SoundEvent event, SoundSource soundSource, float f,float g, RandomSource randomSource, boolean bl, int i, SoundInstance.Attenuation attenuation, double d, double e, double h, boolean bl2) {
        super(event, soundSource, randomSource);
        location = event.getLocation();
        this.volume = f;
        totalVolume = f;
        this.pitch = g;
        this.x = d;
        this.y = e;
        this.z = h;
        this.looping = shouldLoop(location);
        this.delay = i;
        this.attenuation = attenuation;
        this.relative = bl2;
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

    static List<String> loopingSounds = List.of(
            "island.music.parkour_warrior",
            "island.music.classic_hitw"
    );
    static boolean shouldLoop(ResourceLocation sound) {
        return loopingSounds.contains(sound.getPath());
    }
}

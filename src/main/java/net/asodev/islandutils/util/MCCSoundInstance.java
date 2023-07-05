package net.asodev.islandutils.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

public class MCCSoundInstance extends AbstractTickableSoundInstance {

    public static ResourceLocation location;

    public float ticksRemaining;
    public boolean hasLooped;

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
        this.looping = bl;
        this.delay = i;
        this.attenuation = attenuation;
        this.relative = bl2;
        this.ticksRemaining = ticksBeforeLoop(location);
    }

    public void fade(float ticks) {
        isFading = true;
        totalFadeTicks = ticks;
        fadeTicks = totalFadeTicks;
    }
    public void unfade() {
        isFading = false;
        totalFadeTicks = 20f;
        fadeTicks = 0f;
        volume = totalVolume;
    }
    public void stopFwd() {
        stop();
    }

    @Override
    public void tick() {
        if (ticksRemaining != -1 && !hasLooped) {
            if (ticksRemaining <= 0) {
                MCCSoundInstance newInstance = this.copy();
                Minecraft.getInstance().getSoundManager().queueTickingSound(newInstance);
                MusicUtil.currentlyPlayingSound = newInstance;
                hasLooped = true;
                ChatUtils.debug("Looping sound...");
                return;
            }
            ticksRemaining--;
        }

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

    public MCCSoundInstance copy() {
        return new MCCSoundInstance(
                SoundEvent.createVariableRangeEvent(this.getLocation()),
                this.source,
                this.totalVolume,
                this.pitch,
                this.random,
                this.looping,
                this.delay,
                this.attenuation,
                this.x,
                this.y,
                this.z,
                this.relative
        );
    }

    static int ticksBeforeLoop(ResourceLocation sound) {
        if (sound.getPath().equals("island.music.parkour_warrior")) {
            return 2400;
        }
        return -1;
    }
}

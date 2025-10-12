package net.asodev.islandutils.modules.music;

import net.asodev.islandutils.IslandUtilsClient;
import net.asodev.islandutils.mixins.accessors.SoundEngineAccessor;
import net.asodev.islandutils.mixins.accessors.SoundManagerAccessor;
import net.asodev.islandutils.modules.music.modifiers.ClassicHitwMusic;
import net.asodev.islandutils.modules.music.modifiers.HighQualityMusic;
import net.asodev.islandutils.modules.music.modifiers.PreviousDynaballMusic;
import net.asodev.islandutils.modules.music.modifiers.TgttosDomeModifier;
import net.asodev.islandutils.modules.music.modifiers.TgttosDoubleTime;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.options.saving.IslandUtilsSaveHandler;
import net.asodev.islandutils.util.ChatUtils;
import net.asodev.islandutils.util.MCCSoundInstance;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MusicManager {
    private static final int FADE_DURATION = 20;
    private final static List<MusicModifier> modifiersList = new ArrayList<>();
    private static MCCSoundInstance currentlyPlaying = null;

    private static final List<String> loopingTracks = List.of(
            "music.global.parkour_warrior",
            "music.global.battle_box",
            "music.global.dynaball",
            "music.global.hole_in_the_wall",
            "music.global.sky_battle",
            "music.global.tgttosawaf",
            "music.global.overtime_loop_music",

            "music.global.hub_classic",
            "music.global.hub_like",
            "music.global.hubbin",
            "music.global.island",
            "music.global.our_hub",
            "music.global.relax_hub",
            "music.global.we_are"
    );

    public static void init() {
        addModifier(new TgttosDoubleTime());
        addModifier(new TgttosDomeModifier());
        addModifier(new ClassicHitwMusic());
        addModifier(new PreviousDynaballMusic());
        addModifier(new HighQualityMusic());

        ClientTickEvents.START_CLIENT_TICK.register((client) -> {
            if (currentlyPlaying == null) return;
            if (currentlyPlaying.isStopped()) {
                client.getSoundManager().stop(currentlyPlaying);
            }
        });

        IslandOptions.load();
    }


    private static boolean shouldIgnore(SoundInfo info) {
        // ignore tracks that aren't the music tracks we wanna mess with
        return !loopingTracks.contains(info.path().getPath());
    }

    public static void onMusicSoundPacket(ClientboundSoundPacket packet, Minecraft minecraft) {
        startMusic(SoundInfo.fromPacket(packet));
    }

    public static void startMusic(SoundInfo info) {
        if (shouldIgnore(info)) {
            Minecraft.getInstance().getSoundManager().play(info.toSoundInstance());
            return;
        }

        SoundInfo newSoundInfo = applyModifiers(info)
                .withLooping(true);

        if (currentlyPlaying != null) {
            if (newSoundInfo.path().equals(currentlyPlaying.location)) {
                ChatUtils.debug("Cancelled the playing of " + info.path() + " because it was already playing.");
                return;
            }
            currentlyPlaying.fade(FADE_DURATION);
        }

        MCCSoundInstance instance = newSoundInfo.toSoundInstance();
        currentlyPlaying = instance;
        ChatUtils.debug("Starting music: " + instance.getLocation());
        Minecraft.getInstance().getSoundManager().play(instance);
    }

    public static void onMusicStopPacket(ClientboundStopSoundPacket packet, Minecraft minecraft) {
        ResourceLocation name = packet.getName();
        ResourceLocation modifiedName = applyModifiers(SoundInfo.fromLocation(name)).path();

        SoundManager soundManager = Minecraft.getInstance().getSoundManager();
        for (SoundInstance instance : getActiveSoundInstances()) {
            if (instance.getLocation().equals(name) || instance.getLocation().equals(modifiedName)) {
                if (instance instanceof MCCSoundInstance mccSound) {
                    mccSound.fade(FADE_DURATION);
                } else {
                    soundManager.stop(instance);
                }
            }
        }
        if (currentlyPlaying != null && currentlyPlaying.getLocation().equals(name)) {
            currentlyPlaying = null;
        }
    }

    public static SoundInfo applyModifiers(SoundInfo info) {
        SoundInfo modified = info;

        for (MusicModifier modifier : modifiersList) {
            if (!modifier.isEnabled() || !modifier.shouldApply(info.path())) continue;
            modified = modifier.apply(modified);
        }
        return modified;
    }

    public static void stop() {
        currentlyPlaying = null;
    }

    public static List<MusicModifier> getModifiers() {
        return modifiersList;
    }

    private static void addModifier(MusicModifier modifier) {
        modifiersList.add(modifier);
    }

    private static Set<SoundInstance> getActiveSoundInstances() {
        SoundManager soundManager = Minecraft.getInstance().getSoundManager();
        SoundEngine engine = ((SoundManagerAccessor)soundManager).getSoundEngine();
        Map<SoundInstance, ChannelAccess.ChannelHandle> instanceToChannel = ((SoundEngineAccessor)engine).getInstanceToChannel();
        return instanceToChannel.keySet();
    }
}

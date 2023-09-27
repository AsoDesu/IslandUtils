package net.asodev.islandutils.util;

import com.mojang.blaze3d.audio.Listener;
import com.mojang.brigadier.context.CommandContext;
import net.asodev.islandutils.mixins.accessors.SoundEngineAccessor;
import net.asodev.islandutils.mixins.accessors.SoundManagerAccessor;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.options.IslandSoundCategories;
import net.asodev.islandutils.options.categories.MusicOptions;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.state.Game;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

import java.util.*;

import static net.minecraft.network.chat.Component.literal;

public class MusicUtil {

    public static MCCSoundInstance currentlyPlayingSound = null;

    public static void startMusic(ClientboundSoundPacket clientboundSoundPacket) {
        startMusic(clientboundSoundPacket, false);
    }
    public static void startMusic(ClientboundSoundPacket clientboundCustomSoundPacket, boolean bypassOvertimeCheck) {
        MusicOptions options = IslandOptions.getMusic();
        switch (MccIslandState.getGame()) {
            case HITW -> { if (!options.isHitwMusic()) return; }
            case TGTTOS -> { if (!options.isTgttosMusic()) return; }
            case BATTLE_BOX -> { if (!options.isBbMusic()) return; }
            case SKY_BATTLE -> { if (!options.isSbMusic()) return; }
            case PARKOUR_WARRIOR_DOJO -> { if (!options.isPkwMusic()) return; }
            case PARKOUR_WARRIOR_SURVIVOR -> {
                if (!options.isPkwsMusic()) return;
                if (currentlyPlayingSound != null) return;
                if (!bypassOvertimeCheck && isOvertimePlaying()) return;
            }
            case DYNABALL -> { if (!options.isDynaballMusic()) return; }
        }

        ResourceLocation location = MccIslandState.getGame().getMusicLocation();
        if (MccIslandState.getGame() == Game.HITW && IslandOptions.getClassicHITW().isClassicHITWMusic()) {
            location = new ResourceLocation("island","island.music.classic_hitw");
            ChatUtils.send(literal("Now playing: ").withStyle(ChatFormatting.GREEN)
                            .append(literal("Spacewall - Taylor Grover").withStyle(ChatFormatting.AQUA))
            );
        }
        if (location == null) return;

        float pitch = 1f;
        if (options.isTgttosDoubleTime() && MccIslandState.getGame() == Game.TGTTOS &&
            Objects.equals(MccIslandState.getModifier(), "DOUBLE TIME")) {
                pitch = 1.2f;
                ChatUtils.debug("[MusicUtil] Double Time on TGTTOS active! (Pitch: %s)", pitch);
        }
        if (options.isTgttosToTheDome() && MccIslandState.getGame() == Game.TGTTOS &&
                Objects.equals(MccIslandState.getMap(), "TO THE DOME")) {
            location = new ResourceLocation("island", "island.music.to_the_dome");
            ChatUtils.debug("[MusicUtil] To The Dome on TGTTOS active!");
        }

        ChatUtils.debug("[MusicUtil] Starting: " + location);
        stopMusic();

        MCCSoundInstance instance = new MCCSoundInstance(
                SoundEvent.createVariableRangeEvent(location),
                IslandSoundCategories.GAME_MUSIC,
                clientboundCustomSoundPacket.getVolume(),
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
        currentlyPlayingSound = instance;
    }

    public static SimpleSoundInstance createSoundInstance(ClientboundSoundPacket clientboundCustomSoundPacket, SoundSource source) {
        return new SimpleSoundInstance(
                clientboundCustomSoundPacket.getSound().value().getLocation(),
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
    public static SimpleSoundInstance createSoundInstance(ResourceLocation resourceLocation) {
        Vec3 listenerPosition = getListenerPosition();
        return new SimpleSoundInstance(
                resourceLocation,
                IslandSoundCategories.SOUND_EFFECTS,
                1f,
                1f,
                RandomSource.create(),
                false,
                0,
                SoundInstance.Attenuation.NONE,
                listenerPosition.x,
                listenerPosition.y,
                listenerPosition.z,
                false
        );
    }

    private static Vec3 getListenerPosition() {
        SoundEngineAccessor soundEngine = (SoundEngineAccessor)((SoundManagerAccessor)Minecraft.getInstance().getSoundManager()).getSoundEngine();
        Listener listener = soundEngine.getListener();
        return listener.getListenerPosition();
    }

    public static void stopMusic() {
        stopMusic(false);
    }

    public static void stopMusic(Boolean instant) {
        if (currentlyPlayingSound != null) {
            if (!instant) {
                currentlyPlayingSound.fade(20);
                ChatUtils.debug("[MusicUtil] Fading: " + currentlyPlayingSound);
            } else {
                currentlyPlayingSound.stopFwd();
            }
            currentlyPlayingSound = null;
            return;
        }
        ResourceLocation location = MccIslandState.getGame().getMusicLocation();
        if (location == null) return;

        ChatUtils.debug("[MusicUtil] Stopping: " + location.getPath());
        Minecraft.getInstance().getSoundManager().stop(location, IslandSoundCategories.GAME_MUSIC);
    }

    public static void resetMusic(CommandContext<?> ctx) {
        if (currentlyPlayingSound == null) {
            ChatUtils.send(literal("There is no music currently playing.").withStyle(ChatFormatting.RED));
            return;
        }

        ResourceLocation music = currentlyPlayingSound.getLocation();
        float pitch = currentlyPlayingSound.getPitch();
        stopMusic(true);

        Vec3 listenerPosition = getListenerPosition();
        MCCSoundInstance instance = new MCCSoundInstance(
                SoundEvent.createVariableRangeEvent(music),
                IslandSoundCategories.GAME_MUSIC,
                1f,
                pitch,
                RandomSource.create(),
                false,
                0,
                SoundInstance.Attenuation.LINEAR,
                listenerPosition.x,
                listenerPosition.y,
                listenerPosition.z,
                false);
        currentlyPlayingSound = instance;
        Minecraft.getInstance().getSoundManager().play(instance);
        ChatUtils.send(literal("Reset your music!").withStyle(ChatFormatting.GREEN));
    }

    public static boolean isOvertimePlaying() {
        return isSoundsPlaying("music.global.overtime_intro_music", "music.global.overtime_loop_music");
    }
    public static boolean isSoundsPlaying(String ...sounds) {
        List<String> soundList = Arrays.stream(sounds).toList();
        SoundManager soundManager = Minecraft.getInstance().getSoundManager();
        SoundEngineAccessor engine = (SoundEngineAccessor)((SoundManagerAccessor)soundManager).getSoundEngine();
        for (SoundInstance soundInstance : engine.getInstanceToChannel().keySet()) {
            if (!soundList.contains(soundInstance.getLocation().getPath())) continue;
            return true;
        }
        return false;
    }

}

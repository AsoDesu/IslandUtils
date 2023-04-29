package net.asodev.islandutils.util;

import com.mojang.blaze3d.audio.Listener;
import com.mojang.brigadier.context.CommandContext;
import net.asodev.islandutils.mixins.accessors.SoundEngineAccessor;
import net.asodev.islandutils.mixins.accessors.SoundManagerAccessor;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.options.IslandSoundCategories;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.state.GAME;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

import static net.asodev.islandutils.options.IslandOptions.getOptions;
import static net.minecraft.network.chat.Component.literal;

public class MusicUtil {

    public static MCCSoundInstance currentlyPlayingSound = null;

    public static void startMusic(ClientboundSoundPacket clientboundCustomSoundPacket) {
        IslandOptions options = getOptions();
        switch (MccIslandState.getGame()) {
            case HITW -> { if (!options.isHitwMusic()) return; }
            case TGTTOS -> { if (!options.isTgttosMusic()) return; }
            case BATTLE_BOX -> { if (!options.isBbMusic()) return; }
            case SKY_BATTLE -> { if (!options.isSbMusic()) return; }
            case PARKOUR_WARRIOR -> { if (!options.isPkwMusic()) return; }
        }

        ResourceLocation location = MccIslandState.getGame().getMusicLocation();
        if (MccIslandState.getGame() == GAME.HITW && IslandOptions.getOptions().isClassicHITWMusic()) {
            location = new ResourceLocation("island","island.music.classic_hitw");
            ChatUtils.send(literal("Now playing: ").withStyle(ChatFormatting.GREEN)
                            .append(literal("Spacewall - Taylor Grover").withStyle(ChatFormatting.AQUA))
            );
        }
        if (location == null) return;

        ChatUtils.debug("[MusicUtil] Starting: " + location);
        stopMusic();

        float pitch = 1f;
        if (options.isTgttosDoubleTime() &&
            MccIslandState.getGame() == GAME.TGTTOS &&
            Objects.equals(MccIslandState.getModifier(), "DOUBLE TIME")) {
                pitch = 1.2f;
                ChatUtils.debug("[MusicUtil] Double Time on TGTTOS active! (Pitch: %s)", pitch);
        }

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
            } else currentlyPlayingSound.stopFwd();

            currentlyPlayingSound = null;
            return;
        }
        ResourceLocation location = MccIslandState.getGame().getMusicLocation();
        if (location == null) return;

        ChatUtils.debug("[MusicUtil] Stopping: " + location.getPath());
        Minecraft.getInstance().getSoundManager().stop(location, IslandSoundCategories.GAME_MUSIC);
    }

    public static void resetMusic(CommandContext<FabricClientCommandSource> ctx) {
        if (currentlyPlayingSound == null) {
            ctx.getSource().sendError(literal("There is no music currently playing.").withStyle(ChatFormatting.RED));
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
        ctx.getSource().sendFeedback(literal("Reset your music!").withStyle(ChatFormatting.GREEN));
    }

}

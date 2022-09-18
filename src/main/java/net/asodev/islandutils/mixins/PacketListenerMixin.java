package net.asodev.islandutils.mixins;

import net.asodev.islandutils.state.COSMETIC_TYPE;
import net.asodev.islandutils.state.CosmeticState;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.state.STATE;
import net.asodev.islandutils.util.ChatUtils;
import net.asodev.islandutils.util.MusicUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ClientPacketListener.class)
public abstract class PacketListenerMixin {

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "handleAddObjective", at = @At("TAIL"))
    public void handleAddObjective(ClientboundSetObjectivePacket clientboundSetObjectivePacket, CallbackInfo ci) {
        Component displayName = clientboundSetObjectivePacket.getDisplayName();
        if (displayName == null) return;
        String title = displayName.getString();
        if (title == null) return;

        if (title.contains("HOLE IN THE WALL")) {
            MccIslandState.setGame(STATE.HITW);
        } else if (title.contains("TGTTOS")) {
            MccIslandState.setGame(STATE.TGTTOS);
        } else if (title.contains("SKY BATTLE")) {
            MccIslandState.setGame(STATE.SKY_BATTLE);
        } else if (title.contains("BATTLE BOX")) {
            MccIslandState.setGame(STATE.BATTLE_BOX);
        } else {
            MccIslandState.setGame(STATE.HUB);
        }
    }

    @Inject(method = "handleCustomSoundEvent", at = @At("HEAD"), cancellable = true)
    public void handleCustomSoundEvent(ClientboundCustomSoundPacket clientboundCustomSoundPacket, CallbackInfo ci) {
        if (!MccIslandState.isOnline()) return;
        // Create a sound instance of the sound that is being played with this packed
        SoundInstance instance = new SimpleSoundInstance(
                clientboundCustomSoundPacket.getName(),
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

        // Attempt to get the underlying sound file from the played sound
        // We have to do this because Noxcrew obfuscated the sound ids, and may change should the resource pack update
        ResourceLocation soundLoc;
        try {
            WeighedSoundEvents sounds = instance.resolve(Minecraft.getInstance().getSoundManager());
            soundLoc = sounds.getSound(RandomSource.create()).getLocation();
        } catch (Exception e) {
            return;
        }

        // End:
        // games.global.timer.round_end
        // games.global.music.roundendmusic
        // games.global.music.overtime_intro_music
        // games.global.music.overtime_intro_music
        // games.global.music.gameendmusic

        // Start:
        // games.global.countdown.go
        // games.global.music.gameintro

        // If we aren't in a game, dont play music
        if (MccIslandState.getGame() != STATE.HUB) {
            // Use the sound files above to determine what just happend in the game
            if (MccIslandState.getGame() != STATE.BATTLE_BOX) {
                if (Objects.equals(soundLoc.getPath(), "games.global.countdown.go")) {
                    // The game started. Start the music!!
                    MusicUtil.startMusic(clientboundCustomSoundPacket);
                    return;
                }
            } else {
                if (Objects.equals(soundLoc.getPath(), "games.global.music.gameintro")) {
                    MusicUtil.startMusic(clientboundCustomSoundPacket);
                    ci.cancel();
                    return;
                }
            }
            if (Objects.equals(soundLoc.getPath(), "games.global.timer.round_end") ||
                    Objects.equals(soundLoc.getPath(), "games.global.music.roundendmusic") ||
                    Objects.equals(soundLoc.getPath(), "games.global.music.overtime_intro_music") ||
                    Objects.equals(soundLoc.getPath(), "games.global.music.gameendmusic")) {
                // The game ended or is about to end. Stop the music!!
                MusicUtil.stopMusic();
            }
        }
    }

    @Inject(method = "handleContainerContent", at = @At("HEAD"))
    private void containerContent(ClientboundContainerSetContentPacket clientboundContainerSetContentPacket, CallbackInfo ci) {
        if (Minecraft.getInstance().player == null) return;
        if (clientboundContainerSetContentPacket.getContainerId() != 0) return;

        for (int i = 0; i < clientboundContainerSetContentPacket.getItems().size(); i++) {
            ItemStack item = clientboundContainerSetContentPacket.getItems().get(i);
            COSMETIC_TYPE type = CosmeticState.getType(item);
            if (type == COSMETIC_TYPE.ACCESSORY) CosmeticState.prevAccSlot = item;
            else if (type == COSMETIC_TYPE.HAT) CosmeticState.prevHatSlot = item;
        }
    }

    @Inject(method = "handleRespawn", at = @At("HEAD"))
    private void handleRespawn(ClientboundRespawnPacket clientboundRespawnPacket, CallbackInfo ci) {
        LocalPlayer localPlayer = this.minecraft.player;
        ResourceKey<Level> resourceKey = clientboundRespawnPacket.getDimension();
        if (resourceKey != localPlayer.level.dimension()) {
            MusicUtil.stopMusic();
        }
    }

}

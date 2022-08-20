package net.asodev.islandutils.mixins;

import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.state.STATE;
import net.asodev.islandutils.util.ChatUtils;
import net.asodev.islandutils.util.MusicUtil;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundCustomSoundPacket;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ClientPacketListener.class)
public class ScoreboardMixin {

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

    @Inject(method = "handleCustomSoundEvent", at = @At("TAIL"))
    public void handleCustomSoundEvent(ClientboundCustomSoundPacket clientboundCustomSoundPacket, CallbackInfo ci) {
        ResourceLocation loc = clientboundCustomSoundPacket.getName();

        if (MccIslandState.getGame() == STATE.HITW) {
            if (Objects.equals(loc.getPath(), "15")) {
                MusicUtil.startMusic(clientboundCustomSoundPacket);
            } else if (Objects.equals(loc.getPath(), "1m")) {
                MusicUtil.stopMusic();
            }
        } else {
            if (Objects.equals(loc.getPath(), "1f")) {
                MusicUtil.startMusic(clientboundCustomSoundPacket);
            } else if (Objects.equals(loc.getPath(), "1w") || Objects.equals(loc.getPath(), "1i") || Objects.equals(loc.getPath(), "1g")) {
                MusicUtil.stopMusic();
            }
        }
    }

}

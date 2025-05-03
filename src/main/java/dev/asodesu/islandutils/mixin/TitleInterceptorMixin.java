package dev.asodesu.islandutils.mixin;

import dev.asodesu.islandutils.api.extentions.MinecraftExtKt;
import dev.asodesu.islandutils.api.game.GameExtKt;
import dev.asodesu.islandutils.features.ClassicHitw;
import dev.asodesu.islandutils.games.HoleInTheWall;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class TitleInterceptorMixin {

    @Inject(
            method = "setSubtitleText",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/util/thread/BlockableEventLoop;)V",
                    shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    private void setSubtitleText(ClientboundSetSubtitleTextPacket clientboundSetSubtitleTextPacket, CallbackInfo ci) {
        if (!MinecraftExtKt.isOnline()) return;
        if (GameExtKt.getActiveGame() instanceof HoleInTheWall) {
            if (ClassicHitw.INSTANCE.handleSubtitle(clientboundSetSubtitleTextPacket.text()))
                ci.cancel();
        }
    }

}

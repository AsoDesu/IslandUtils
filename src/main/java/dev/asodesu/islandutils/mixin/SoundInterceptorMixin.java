package dev.asodesu.islandutils.mixin;

import dev.asodesu.islandutils.Modules;
import dev.asodesu.islandutils.api.MinecraftExtKt;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class SoundInterceptorMixin {

    @Inject(
            method = "handleSoundEvent",
            at = @At(
                    value = "INVOKE", // remove potential duplicates
                    target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/util/thread/BlockableEventLoop;)V",
                    shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    public void handleSoundEvent(ClientboundSoundPacket clientboundSoundPacket, CallbackInfo ci) {
        if (!MinecraftExtKt.isOnline()) return;

        ResourceLocation sound = clientboundSoundPacket.getSound().value().location();
        if (!sound.getNamespace().equals("mcc")) return; // only mcc sounds
        if(Modules.INSTANCE.getMusicManager().handlePacket(clientboundSoundPacket))
            ci.cancel();
    }

    @Inject(
            method = "handleStopSoundEvent",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/util/thread/BlockableEventLoop;)V",
                    shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    public void handleSoundEvent(ClientboundStopSoundPacket clientboundStopSoundPacket, CallbackInfo ci) {
        if (!MinecraftExtKt.isOnline()) return;

        if (Modules.INSTANCE.getMusicManager().handlePacket(clientboundStopSoundPacket))
            ci.cancel();
    }

}

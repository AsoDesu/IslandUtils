package dev.asodesu.islandutils.mixin;

import dev.asodesu.islandutils.api.events.sound.SoundEvents;
import dev.asodesu.islandutils.api.events.sound.SoundStopCallback;
import dev.asodesu.islandutils.api.events.sound.info.SoundInfo;
import dev.asodesu.islandutils.api.events.sound.SoundPlayCallback;
import dev.asodesu.islandutils.api.extentions.MinecraftExtKt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class SoundInterceptorMixin extends ClientCommonPacketListenerImpl {
    protected SoundInterceptorMixin(Minecraft minecraft, Connection connection, CommonListenerCookie commonListenerCookie) {
        super(minecraft, connection, commonListenerCookie);
    }

    @Inject(
            method = "handleSoundEvent",
            at = @At(
                    value = "INVOKE", // remove potential duplicates
                    target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/network/PacketProcessor;)V",
                    shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    public void handleSoundEvent(ClientboundSoundPacket clientboundSoundPacket, CallbackInfo ci) {
        if (!MinecraftExtKt.isOnline()) return;

        final SoundInfo[] replacedInfo = {null};
        SoundInfo info = SoundInfo.Companion.fromPacket(clientboundSoundPacket);
        SoundPlayCallback.Info callbackInfo = new SoundPlayCallback.Info() {
            @Override
            public void replace(@NotNull SoundInfo info) {
                replacedInfo[0] = info;
            }

            @Override
            public void cancel() {
                ci.cancel();
            }
        };
        SoundEvents.getSOUND_PLAY().invoker().onSoundPlay(info, callbackInfo);

        SoundInfo newSoundInfo = replacedInfo[0];
        if (newSoundInfo != null) {
            ci.cancel();
            this.minecraft.level.playSeededSound(
                            this.minecraft.player,
                            clientboundSoundPacket.getX(),
                            clientboundSoundPacket.getY(),
                            clientboundSoundPacket.getZ(),
                            newSoundInfo.toSoundEvent(),
                            newSoundInfo.getCategory(),
                            newSoundInfo.getVolume(),
                            newSoundInfo.getPitch(),
                            clientboundSoundPacket.getSeed()
                    );
        }
    }

    @Inject(
            method = "handleStopSoundEvent",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/network/PacketProcessor;)V",
                    shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    public void handleSoundEvent(ClientboundStopSoundPacket clientboundStopSoundPacket, CallbackInfo ci) {
        if (!MinecraftExtKt.isOnline()) return;

        SoundStopCallback.StopInfo info = new SoundStopCallback.StopInfo(
                clientboundStopSoundPacket.getName(),
                clientboundStopSoundPacket.getSource()
        );
        SoundStopCallback.Info callbackInfo = ci::cancel;
        SoundEvents.getSOUND_STOP().invoker().onSoundStop(info, callbackInfo);
    }

}

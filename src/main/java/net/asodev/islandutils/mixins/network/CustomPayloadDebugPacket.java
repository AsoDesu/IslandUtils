package net.asodev.islandutils.mixins.network;

import io.netty.buffer.ByteBuf;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.util.ChatUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(ClientboundCustomPayloadPacket.class)
public class CustomPayloadDebugPacket {

    @Inject(method = "readPayload", at = @At("HEAD"))
    private static void readPayloadInject(ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf, CallbackInfoReturnable<CustomPacketPayload> cir) {
        if (!IslandOptions.getMisc().isDebugMode()) return;

        ChatUtils.debug(resourceLocation + " -> " + friendlyByteBuf.readableBytes() + "bytes");
    }

}

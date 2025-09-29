package net.asodev.islandutils.mixins.network;

import io.netty.channel.ChannelHandlerContext;
import net.asodev.islandutils.IslandUtilsEvents;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public class ConnectionMixin {

    @Inject(at = @At("HEAD"), method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/protocol/Packet;)V", cancellable = true)
    public void channelRead(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci) {
        IslandUtilsEvents.PACKET_RECEIVED.invoker().onPacketReceived(packet, ci);
    }

}

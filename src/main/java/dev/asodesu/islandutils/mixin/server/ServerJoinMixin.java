package dev.asodesu.islandutils.mixin.server;

import dev.asodesu.islandutils.api.server.ServerSessionHandler;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.protocol.login.ClientboundLoginFinishedPacket;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientHandshakePacketListenerImpl.class)
public class ServerJoinMixin {

    @Shadow
    @Final
    private @Nullable ServerData serverData;

    @Inject(method = "handleLoginFinished", at = @At("RETURN"))
    private void handleLoginFinished(ClientboundLoginFinishedPacket clientboundLoginFinishedPacket, CallbackInfo ci) {
        if (serverData == null) return;
        ServerSessionHandler.INSTANCE.onConnect(serverData);
    }

}

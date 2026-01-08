package dev.asodesu.islandutils.mixin.server;

import dev.asodesu.islandutils.IslandUtils;
import dev.asodesu.islandutils.api.extentions.MinecraftExtKt;
import dev.asodesu.islandutils.api.server.ServerSessionHandler;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.network.protocol.login.ClientboundLoginFinishedPacket;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class InstanceJoinMixin {

    @Inject(method = "handleLogin", at = @At("RETURN"))
    private void handleLogin(ClientboundLoginPacket clientboundLoginPacket, CallbackInfo ci) {
        if (!MinecraftExtKt.isOnline()) return;
        ServerSessionHandler.INSTANCE.onInstanceJoin(clientboundLoginPacket);
    }

}

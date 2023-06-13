package net.asodev.islandutils.mixins.discord;

import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.protocol.login.ClientboundGameProfilePacket;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.asodev.islandutils.IslandUtilsClient.onJoinMCCI;

@Mixin(ClientHandshakePacketListenerImpl.class)
public class JoinMCCIMixin {

    @Shadow @Final private @Nullable ServerData serverData;

    @Inject(method = "handleGameProfile", at = @At("HEAD"))
    private void handleGameProfile(ClientboundGameProfilePacket clientboundGameProfilePacket, CallbackInfo ci) {
        if (this.serverData == null) return;
        if (!this.serverData.ip.toLowerCase().contains("mccisland")) return;
        onJoinMCCI();
    }

}

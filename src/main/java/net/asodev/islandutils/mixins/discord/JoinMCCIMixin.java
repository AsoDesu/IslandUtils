package net.asodev.islandutils.mixins.discord;

import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.protocol.login.ClientboundLoginFinishedPacket;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.asodev.islandutils.IslandUtilsClient.onJoinMCCI;
import static net.asodev.islandutils.util.Utils.isProdMCCI;

@Mixin(ClientHandshakePacketListenerImpl.class)
public class JoinMCCIMixin {
    @Shadow @Final private @Nullable ServerData serverData;

    @Inject(method = "handleLoginFinished", at = @At("HEAD"))
    private void handleGameProfile(ClientboundLoginFinishedPacket clientboundLoginFinishedPacket, CallbackInfo ci) {
        if (this.serverData == null) return;
        if (!this.serverData.ip.toLowerCase().contains("mccisland")) return;

        onJoinMCCI(isProdMCCI(this.serverData.ip.toLowerCase()));
    }

}

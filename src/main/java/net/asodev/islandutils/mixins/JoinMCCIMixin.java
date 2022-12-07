package net.asodev.islandutils.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.asodev.islandutils.IslandutilsClient.onJoinMCCI;

@Mixin(Minecraft.class)
public class JoinMCCIMixin {

    @Inject(method = "setCurrentServer(Lnet/minecraft/client/multiplayer/ServerData;)V", at = @At("TAIL"))
    private void setServer(ServerData serverData, CallbackInfo ci) {
        if (serverData == null) return;
        if (!serverData.ip.toLowerCase().contains("mccisland")) return;
        onJoinMCCI();
    }

}

package net.asodev.islandutils.mixins.network;

import com.noxcrew.noxesium.network.clientbound.ClientboundMccGameStatePacket;
import com.noxcrew.noxesium.network.clientbound.ClientboundMccServerPacket;
import net.asodev.islandutils.IslandUtilsEvents;
import net.asodev.islandutils.state.Game;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.util.ChatUtils;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.player.LocalPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientboundMccGameStatePacket.class)
public class GamePacketMixin {
    @Inject(method = "receive", at = @At("HEAD"))
    private void onReceive(LocalPlayer player, PacketSender responseSender, CallbackInfo ci) {
        ClientboundMccGameStatePacket packet = (ClientboundMccGameStatePacket)(Object)this;

        // I have no use for this right now, but i'll keep it here just in case :3
        IslandUtilsEvents.GAME_STATE_CHANGE.invoker().onGameUpdate(packet);
        ChatUtils.debug(" &bStage: &3" + packet.stage + " &6PhaseType&e: " + packet.phaseType);
    }
}

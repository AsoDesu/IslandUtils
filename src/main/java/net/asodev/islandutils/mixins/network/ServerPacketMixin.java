package net.asodev.islandutils.mixins.network;

import com.noxcrew.noxesium.network.clientbound.ClientboundMccServerPacket;
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

@Mixin(ClientboundMccServerPacket.class)
public class ServerPacketMixin {
    private static Logger LOGGER = LoggerFactory.getLogger(ServerPacketMixin.class);

    @Inject(method = "receive", at = @At("HEAD"))
    private void onReceive(LocalPlayer player, PacketSender responseSender, CallbackInfo ci) {
        ClientboundMccServerPacket packet = (ClientboundMccServerPacket)(Object)this;
        ChatUtils.debug("Received server packet " + packet.associatedGame + " (" + packet.type + "/" + packet.subType + ")");
        if (packet.type.startsWith("lobby")) {
            MccIslandState.setGame(Game.HUB);
        } else {
            try {
                MccIslandState.setGame(Game.fromPacket(packet));
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
    }
}

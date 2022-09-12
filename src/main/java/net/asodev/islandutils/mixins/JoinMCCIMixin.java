package net.asodev.islandutils.mixins;

import net.asodev.islandutils.IslandUtils;
import net.asodev.islandutils.util.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.SoundOptionsScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.game.ClientboundServerDataPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class JoinMCCIMixin {

    @Inject(method = "setCurrentServer(Lnet/minecraft/client/multiplayer/ServerData;)V", at = @At("TAIL"))
    public void handleServerData(ServerData serverData, CallbackInfo ci) {
        if (serverData == null) return;
        if (serverData.ip == null) return;

        if (serverData.ip.contains("mccisland")) {
            if (IslandUtils.availableUpdate != null) {
                ChatUtils.dev("Hey! Update " + IslandUtils.availableUpdate.title() + " is available for Island Utils!");

                Style style = Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, IslandUtils.availableUpdate.releaseUrl()));
                Component link = Component.literal(IslandUtils.availableUpdate.releaseUrl()).setStyle(style);
                Component text = Component.literal(ChatUtils.translate(ChatUtils.prefix + " Download Here: &f")).append(link);

                ChatUtils.send(text);
            }
        }
    }

}

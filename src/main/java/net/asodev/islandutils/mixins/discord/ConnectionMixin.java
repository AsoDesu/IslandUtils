package net.asodev.islandutils.mixins.discord;

import net.asodev.islandutils.IslandUtilsEvents;
import net.asodev.islandutils.discord.DiscordPresenceUpdator;
import net.asodev.islandutils.state.Game;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.util.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

@Mixin(Connection.class)
public abstract class ConnectionMixin {

    @Shadow public abstract SocketAddress getRemoteAddress();

    @Shadow @Nullable private volatile PacketListener packetListener;

    @Inject(method = "disconnect(Lnet/minecraft/network/chat/Component;)V", at = @At("HEAD"))
    private void disconnect(Component component, CallbackInfo ci) {
        SocketAddress remoteAddress = getRemoteAddress();
        if (remoteAddress == null) return;
        if (remoteAddress instanceof InetSocketAddress socketAddress) {
            String hostName = socketAddress.getHostName();
            if (hostName == null) return;
            Minecraft minecraft = Minecraft.getInstance();
            if (hostName.contains("mccisland.net") && packetListener == minecraft.getConnection()) {
                ChatUtils.debug("Disconnected from MCC Island.");
                DiscordPresenceUpdator.started = null;
                DiscordPresenceUpdator.clear();
                IslandUtilsEvents.QUIT_MCCI.invoker().onEvent();
                MccIslandState.setGame(Game.HUB);
            }
        }
    }

}

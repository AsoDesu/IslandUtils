package net.asodev.islandutils.mixins.discord;

import net.asodev.islandutils.discord.DiscordPresenceUpdator;
import net.asodev.islandutils.state.GAME;
import net.asodev.islandutils.state.MccIslandState;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
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

    @Inject(method = "disconnect", at = @At("HEAD"))
    private void disconnect(Component component, CallbackInfo ci) {
        if (getRemoteAddress() == null) return;
        String hostName = ((InetSocketAddress) getRemoteAddress()).getHostName();
        if (hostName == null) return;
        if (hostName.contains("mccisland.net")) {
            DiscordPresenceUpdator.started = null;
            DiscordPresenceUpdator.clear();
            MccIslandState.setGame(GAME.HUB);
        }
    }

}

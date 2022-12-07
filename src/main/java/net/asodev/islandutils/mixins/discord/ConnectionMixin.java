package net.asodev.islandutils.mixins.discord;

import net.asodev.islandutils.discord.DiscordPresence;
import net.asodev.islandutils.discord.DiscordPresenceUpdator;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public class ConnectionMixin {

    @Inject(method = "disconnect", at = @At("HEAD"))
    private void disconnect(Component component, CallbackInfo ci) {
        DiscordPresenceUpdator.started = null;
        DiscordPresenceUpdator.clear();
    }

}

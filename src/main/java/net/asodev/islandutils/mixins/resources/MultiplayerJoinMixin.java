package net.asodev.islandutils.mixins.resources;

import net.asodev.islandutils.IslandUtils;
import net.asodev.islandutils.resourcepack.ResourcePackUpdater;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.resources.Resource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JoinMultiplayerScreen.class)
public class MultiplayerJoinMixin extends Screen {

    protected MultiplayerJoinMixin(Component component) { super(component); }

    @Inject(method = "join", at = @At("HEAD"), cancellable = true)
    private void join(ServerData serverData, CallbackInfo ci) {
        if (!IslandUtils.packUpdater.accepted && (IslandUtils.packUpdater.getting || ResourcePackUpdater.pack == null)) {
            ci.cancel();

            Component message = Component.literal("IslandUtils Music has not been downloaded").withStyle(ChatFormatting.AQUA);
            Component title = Component.literal("Proceed?").withStyle(ChatFormatting.RED);
            Component no = Component.literal("Cancel");
            Component yes = Component.literal("Continue joining");

            ConfirmScreen screen = new ConfirmScreen((boo) -> confirm(boo, serverData), message, title, yes, no);
            this.minecraft.setScreen(screen);
        }
    }

    void confirm(boolean bool, ServerData serverData) {
        if (bool) {
            IslandUtils.packUpdater.accepted = true;
            ConnectScreen.startConnecting(this, this.minecraft, ServerAddress.parseString(serverData.ip), serverData);
        }
        else this.minecraft.setScreen(this);
    }

}

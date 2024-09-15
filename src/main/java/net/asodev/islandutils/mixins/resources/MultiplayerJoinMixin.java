package net.asodev.islandutils.mixins.resources;

import net.asodev.islandutils.IslandUtils;
import net.asodev.islandutils.util.resourcepack.ResourcePackUpdater;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.chat.Component;
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

            Component message = Component.translatable("islandutils.message.music.notDownloadedWarn").withStyle(ChatFormatting.AQUA);
            Component title = Component.translatable("islandutils.message.music.notDownloadedWarnTitle").withStyle(ChatFormatting.RED);
            Component no = Component.translatable("islandutils.message.music.notDownloadedWarnCancel");
            Component yes = Component.translatable("islandutils.message.music.notDownloadedWarnContinue");

            ConfirmScreen screen = new ConfirmScreen((boo) -> confirm(boo, serverData), message, title, yes, no);
            this.minecraft.setScreen(screen);
        }
    }

    void confirm(boolean bool, ServerData serverData) {
        if (bool) {
            IslandUtils.packUpdater.accepted = true;
            ConnectScreen.startConnecting(this, this.minecraft, ServerAddress.parseString(serverData.ip), serverData, false, null);
        }
        else this.minecraft.setScreen(this);
    }

}

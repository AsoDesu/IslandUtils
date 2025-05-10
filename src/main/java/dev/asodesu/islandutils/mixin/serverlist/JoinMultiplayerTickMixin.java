package dev.asodesu.islandutils.mixin.serverlist;

import dev.asodesu.islandutils.api.notifier.Notifier;
import dev.asodesu.islandutils.api.notifier.ServerListNotificationRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JoinMultiplayerScreen.class)
public class JoinMultiplayerTickMixin {

    @Inject(method = "init", at = @At("HEAD"))
    private void init(CallbackInfo ci) {
        Notifier.INSTANCE.activate();
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void render(CallbackInfo ci) {
        Notifier.INSTANCE.tickActive();
    }

}

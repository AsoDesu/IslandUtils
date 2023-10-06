package net.asodev.islandutils.mixins.reporting;

import net.asodev.islandutils.modules.reporting.ReportHandler;
import net.asodev.islandutils.modules.reporting.playersui.ReportingPlayersScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.social.SocialInteractionsScreen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow public abstract void setScreen(@Nullable Screen screen);

    @Inject(
        method = "setScreen",
        at = @At("HEAD"),
        cancellable = true
    )
    private void setScreen(Screen screen, CallbackInfo ci) {
        if (screen instanceof SocialInteractionsScreen && ReportHandler.shouldChangeReporting()) {
            setScreen(new ReportingPlayersScreen());
            ci.cancel();
        }
    }
}

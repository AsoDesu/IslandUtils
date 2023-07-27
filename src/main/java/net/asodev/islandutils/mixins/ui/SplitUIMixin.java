package net.asodev.islandutils.mixins.ui;

import net.asodev.islandutils.state.splits.LevelSplit;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossHealthOverlay.class)
public class SplitUIMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void render(GuiGraphics guiGraphics, CallbackInfo ci) {
        LevelSplit instance = LevelSplit.getInstance();
        if (instance != null) instance.getUI().render(guiGraphics);
    }

}

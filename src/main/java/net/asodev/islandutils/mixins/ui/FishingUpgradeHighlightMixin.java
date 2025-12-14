package net.asodev.islandutils.mixins.ui;

import net.asodev.islandutils.modules.FishingUpgradeIcon;
import net.asodev.islandutils.options.IslandOptions;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public class FishingUpgradeHighlightMixin {
    @Inject(method = "renderSlot", at = @At(value = "TAIL"))
    private void renderSlot(GuiGraphics guiGraphics, Slot slot, CallbackInfo ci) {
        if (!slot.hasItem() || !IslandOptions.getMisc().isShowFishingUpgradeIcon()) return;
        FishingUpgradeIcon.render(slot, guiGraphics);
    }
}

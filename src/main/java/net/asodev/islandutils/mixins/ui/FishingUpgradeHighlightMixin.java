package net.asodev.islandutils.mixins.ui;

import net.asodev.islandutils.modules.FishingUpgradeIcon;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.state.Game;
import net.asodev.islandutils.state.MccIslandState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;

@Mixin(AbstractContainerScreen.class)
public class FishingUpgradeHighlightMixin {
    @Inject(method = "extractSlot", at = @At(value = "TAIL"))
    private void renderSlot(GuiGraphicsExtractor guiGraphics, Slot slot, int i, int j, CallbackInfo ci) {
        if (!slot.hasItem() || !IslandOptions.getMisc().isShowFishingUpgradeIcon()) return;
        if (MccIslandState.getGame() != Game.FISHING) return;
        FishingUpgradeIcon.render(slot, guiGraphics);
    }
}

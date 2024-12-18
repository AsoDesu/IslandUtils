package net.asodev.islandutils.mixins.ui;

import net.asodev.islandutils.modules.FishingUpgradeIcon;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.state.Game;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.util.Utils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(AbstractContainerScreen.class)
public class FishingUpgradeHighlightMixin {
    @Inject(method = "renderSlot", at = @At(value = "TAIL"))
    private void renderSlot(GuiGraphics guiGraphics, Slot slot, CallbackInfo ci) {
        if (!slot.hasItem() || !IslandOptions.getMisc().isShowFishingUpgradeIcon()) return;
        if (MccIslandState.getGame() != Game.FISHING) return;
        FishingUpgradeIcon.render(slot, guiGraphics);
    }
}

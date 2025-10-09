package net.asodev.islandutils.mixins.crafting;

import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.util.Utils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(AbstractContainerScreen.class)
public class RemnantHighlightMixin {

    @Inject(method = "renderSlot", at = @At("HEAD"))
    private void renderSlot(GuiGraphics guiGraphics, Slot slot, CallbackInfo ci) {
        if (!MccIslandState.isOnline()) return;
        if (!slot.hasItem()) return;

        List<Component> lore = Utils.getTooltipLines(slot.getItem());
        if (lore == null) return;
        boolean isRemnant = lore.stream().anyMatch(c -> c.getString().contains("This item is the remnant of an item"));
        if (!isRemnant) return;

        int x = slot.x;
        int y = slot.y;
        guiGraphics.fill(RenderPipelines.GUI, x, y, x + 16, y + 16, ARGB.opaque(9257316));
    }

}

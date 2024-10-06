package net.asodev.islandutils.mixins.crafting;

import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.util.Utils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
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

        List<Component> lore = Utils.getLores(slot.getItem());
        if (lore == null) return;
        boolean isRemnant = lore.stream().anyMatch(c -> c.getString().contains("This item is the remnant of an item"));
        if (!isRemnant) return;

        int x = slot.x;
        int y = slot.y;
        int color = FastColor.ARGB32.color(255, 141, 65, 100);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0f, 0.0f, 105.0f);
        guiGraphics.fill(RenderType.gui(), x, y, x + 16, y + 16, 0, color);
        guiGraphics.pose().popPose();
    }

}

package net.asodev.islandutils.mixins.cosmetics;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.asodev.islandutils.IslandutilsClient;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.state.*;
import net.asodev.islandutils.state.cosmetics.Cosmetic;
import net.asodev.islandutils.state.cosmetics.CosmeticSlot;
import net.asodev.islandutils.state.cosmetics.CosmeticState;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.asodev.islandutils.state.cosmetics.CosmeticState.customModelData;
import static net.asodev.islandutils.state.cosmetics.CosmeticState.itemsMatch;


@Mixin(AbstractContainerScreen.class)
public abstract class ChestScreenMixin extends Screen {
    private static final ResourceLocation PREVIEW = new ResourceLocation("island", "textures/preview.png");

    @Shadow protected Slot hoveredSlot;

    protected ChestScreenMixin(Component component) {
        super(component);
    }

    @Inject(method = "renderSlot", at = @At("TAIL"))
    private void renderSlot(PoseStack poseStack, Slot slot, CallbackInfo ci) {
        if (!MccIslandState.isOnline()) return;

        if (slot.getItem() == null) return;
        ItemStack slotItem = slot.getItem();

        CompoundTag slotTag = slotItem.getTag();
        if (slotTag == null) return;

        boolean shouldRender = false;

        if (CosmeticState.hatSlot.preview != null && itemsMatch(slot.getItem(), CosmeticState.hatSlot.preview.item)) shouldRender = true;
        else if (CosmeticState.accessorySlot.preview != null && itemsMatch(slot.getItem(), CosmeticState.accessorySlot.preview.item)) shouldRender = true;

        if (shouldRender) {
            this.setBlitOffset(395);
            this.itemRenderer.blitOffset = 105.0F;
            RenderSystem.setShaderTexture(0, PREVIEW);
            blit(poseStack, slot.x-3, slot.y-4, this.getBlitOffset(), 0, 0, 22, 24, 22, 24);
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void render(PoseStack poseStack, int i, int j, float f, CallbackInfo ci) {
        if (IslandOptions.getOptions().isShowOnHover() && hoveredSlot != null && hoveredSlot.hasItem() && hoveredSlot.getItem().is(Items.LEATHER_HELMET)) {
            Integer color = CosmeticState.getColor(hoveredSlot.getItem());
            if (color != null) {
                CosmeticState.hoveredColor = color;
                return;
            }
        }
        CosmeticState.hoveredColor = null;
    }

    @Inject(method = "mouseDragged", at = @At("HEAD"))
    private void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY, CallbackInfoReturnable<Boolean> cir) {
        if (!MccIslandState.isOnline()) return;

        CosmeticState.yRot = CosmeticState.yRot - Double.valueOf(deltaX).floatValue();
        CosmeticState.xRot = CosmeticState.xRot - Double.valueOf(deltaY).floatValue();
    }

    @Inject(method = "keyPressed", at = @At("HEAD"))
    private void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (!MccIslandState.isOnline()) return;
        if (hoveredSlot == null || !hoveredSlot.hasItem()) return;

        InputConstants.Key previewBind = KeyBindingHelper.getBoundKeyOf(IslandutilsClient.previewKeyBind);
        if (keyCode == previewBind.getValue()) {
            if (hoveredSlot.getItem().is(Items.GHAST_TEAR) || hoveredSlot.getItem().is(Items.AIR)) return;
            COSMETIC_TYPE type = CosmeticState.getType(hoveredSlot.getItem());

            int hoverCMD = customModelData(hoveredSlot.getItem());

            if (type == COSMETIC_TYPE.HAT) setOrNotSet(CosmeticState.hatSlot, hoverCMD);
            else if (type == COSMETIC_TYPE.ACCESSORY) setOrNotSet(CosmeticState.accessorySlot, hoverCMD);
        }
    }

    private void setOrNotSet(Cosmetic cosmetic, int itemCMD) {
        if (cosmetic.preview == null || itemCMD != customModelData(cosmetic.preview.item))
            cosmetic.preview = new CosmeticSlot(hoveredSlot);
        else
            cosmetic.preview = null;
    }

    @Inject(method = "onClose", at = @At("TAIL"))
    private void onClose(CallbackInfo ci) {
        CosmeticState.hatSlot = new Cosmetic(COSMETIC_TYPE.HAT);
        CosmeticState.accessorySlot = new Cosmetic(COSMETIC_TYPE.ACCESSORY);

        CosmeticState.inspectingPlayer = null;

        CosmeticState.yRot = 155;
        CosmeticState.xRot = -5;
    }

}

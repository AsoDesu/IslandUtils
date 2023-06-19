package net.asodev.islandutils.mixins.cosmetics;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.state.COSMETIC_TYPE;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.state.cosmetics.Cosmetic;
import net.asodev.islandutils.state.cosmetics.CosmeticSlot;
import net.asodev.islandutils.state.cosmetics.CosmeticState;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static net.asodev.islandutils.state.cosmetics.CosmeticState.customModelData;


@Mixin(AbstractContainerScreen.class)
public abstract class ChestScreenMixin extends Screen {
    private static final ResourceLocation PREVIEW = new ResourceLocation("island", "textures/preview.png");

    @Shadow protected Slot hoveredSlot;

    @Shadow @Final protected AbstractContainerMenu menu;

    protected ChestScreenMixin(Component component) {
        super(component);
    }

    // Don't render MCCI's invisible Tooltips (something is conflicting and idk what it is)
    @Inject(method = "renderTooltip", at = @At("HEAD"), cancellable = true)
    private void renderTooltip(PoseStack poseStack, int i, int j, CallbackInfo ci) {
        if (!MccIslandState.isOnline()) return;
        ci.cancel();
        if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
            List<Component> list = this.hoveredSlot.getItem().getTooltipLines(this.minecraft.player, TooltipFlag.NORMAL);
            if (list.size() > 400) return;
            this.renderTooltip(poseStack, this.hoveredSlot.getItem(), i, j);
        }
    }

    @Inject(method = "renderSlot", at = @At("TAIL"))
    private void renderSlot(PoseStack poseStack, Slot slot, CallbackInfo ci) {
        if (!MccIslandState.isOnline()) return;
        ItemStack slotItem = slot.getItem();

        CompoundTag slotTag = slotItem.getTag();
        if (slotTag == null) return;

        boolean shouldRender = false;

        if (CosmeticState.hatSlot.content != null && CosmeticState.hatSlot.content.matchesSlot(slot)) shouldRender = true;
        else if (CosmeticState.accessorySlot.content != null && CosmeticState.accessorySlot.content.matchesSlot(slot)) shouldRender = true;

        if (shouldRender) {
            poseStack.pushPose();
            poseStack.translate(0.0F, 0.0F, 105.0F);
            RenderSystem.setShaderTexture(0, PREVIEW);
            blit(poseStack, slot.x-3, slot.y-4, 0, 0, 0, 22, 24, 22, 24);
            poseStack.popPose();
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void render(PoseStack poseStack, int i, int j, float f, CallbackInfo ci) {
        if (hoveredSlot != null && hoveredSlot.hasItem() && CosmeticState.isColoredItem(hoveredSlot.getItem())) {
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
        triggerPreviewClicked(keyCode);
    }
    @Inject(method = "mouseReleased", at = @At("HEAD"))
    private void mouseReleased(double d, double e, int keyCode, CallbackInfoReturnable<Boolean> cir) {
        triggerPreviewClicked(keyCode);
    }

    private void triggerPreviewClicked(int keyCode) {
        if (!MccIslandState.isOnline()) return;
        if (hoveredSlot == null || !hoveredSlot.hasItem()) return;
        InputConstants.Key previewBind = KeyBindingHelper.getBoundKeyOf(minecraft.options.keyPickItem);
        if (keyCode == previewBind.getValue()) {
            ItemStack item = hoveredSlot.getItem();
            if (item.is(Items.GHAST_TEAR) || item.is(Items.AIR)) return;
            if (!CosmeticState.isLockedItem(item))
                setPreview(item);
        }
    }

    private void setPreview(ItemStack item) {
        COSMETIC_TYPE type = CosmeticState.getType(item);
        int hoverCMD = customModelData(item);
        if (type == COSMETIC_TYPE.HAT) setOrNotSet(CosmeticState.hatSlot, hoverCMD);
        else if (type == COSMETIC_TYPE.ACCESSORY) setOrNotSet(CosmeticState.accessorySlot, hoverCMD);

    }

    private void setOrNotSet(Cosmetic cosmetic, int itemCMD) {
        if (cosmetic.content == null || itemCMD != customModelData(cosmetic.content.item))
            cosmetic.content = new CosmeticSlot(hoveredSlot);
        else
            cosmetic.content = null;
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

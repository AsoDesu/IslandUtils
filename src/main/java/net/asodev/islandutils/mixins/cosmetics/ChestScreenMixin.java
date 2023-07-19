package net.asodev.islandutils.mixins.cosmetics;

import com.mojang.blaze3d.platform.InputConstants;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.state.COSMETIC_TYPE;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.state.cosmetics.Cosmetic;
import net.asodev.islandutils.state.cosmetics.CosmeticSlot;
import net.asodev.islandutils.state.cosmetics.CosmeticState;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static net.asodev.islandutils.util.Utils.customModelData;


@Mixin(AbstractContainerScreen.class)
public abstract class ChestScreenMixin extends Screen {
    private static final ResourceLocation PREVIEW = new ResourceLocation("island", "textures/preview.png");

    @Shadow protected Slot hoveredSlot;

    @Shadow @Final protected AbstractContainerMenu menu;

    @Shadow protected abstract List<Component> getTooltipFromContainerItem(ItemStack itemStack);

    protected ChestScreenMixin(Component component) {
        super(component);
    }

    // Don't render MCCI's invisible Tooltips (something is conflicting and idk what it is)
    @Inject(method = "renderTooltip", at = @At("HEAD"), cancellable = true)
    private void renderTooltip(GuiGraphics guiGraphics, int i, int j, CallbackInfo ci) {
        if (!MccIslandState.isOnline()) return;
        ci.cancel();
        if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
            ItemStack itemStack = this.hoveredSlot.getItem();
            List<Component> lines = this.getTooltipFromContainerItem(itemStack);
            if (lines.size() > 400) return;
            guiGraphics.renderTooltip(this.font, lines, itemStack.getTooltipImage(), i, j);
        }
    }

    @Inject(method = "renderSlot", at = @At("TAIL"))
    private void renderSlot(GuiGraphics guiGraphics, Slot slot, CallbackInfo ci) {
        if (!MccIslandState.isOnline()) return;
        ItemStack slotItem = slot.getItem();

        CompoundTag slotTag = slotItem.getTag();
        if (slotTag == null) return;

        boolean shouldRender = false;

        if (CosmeticState.hatSlot.preview != null && CosmeticState.hatSlot.preview.matchesSlot(slot)) shouldRender = true;
        else if (CosmeticState.accessorySlot.preview != null && CosmeticState.accessorySlot.preview.matchesSlot(slot)) shouldRender = true;

        guiGraphics.pose().pushPose();
        if (shouldRender) {
            guiGraphics.pose().translate(0.0f, 0.0f, 105f);
            guiGraphics.blit(PREVIEW, slot.x-3, slot.y-4, 105, 0, 0, 22, 24, 22, 24);
        }
        guiGraphics.pose().popPose();
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void render(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) {

        if (hoveredSlot != null && hoveredSlot.hasItem()) {
            ItemStack hoveredItem = hoveredSlot.getItem();
            if (IslandOptions.getOptions().isShowCosmeticsOnHover()) {
                if (!CosmeticState.canPreviewItem(hoveredItem) || !setHovered(hoveredItem)) {
                    CosmeticState.hatSlot.hover = null;
                    CosmeticState.accessorySlot.hover = null;
                }
            }

            if (CosmeticState.isColoredItem(hoveredItem)) {
                Integer color = CosmeticState.getColor(hoveredSlot.getItem());
                if (color != null) {
                    CosmeticState.hoveredColor = color;
                    return;
                }
            }
        }
        CosmeticState.hoveredColor = null;
    }

    private boolean setHovered(ItemStack item) {
        COSMETIC_TYPE type = CosmeticState.getType(item);
        if (type == null) return false;
        Cosmetic cosmeticByType = CosmeticState.getCosmeticByType(type);
        if (cosmeticByType == null) return false;
        cosmeticByType.hover = new CosmeticSlot(item, hoveredSlot);
        return true;
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

    @Inject(method = "slotClicked", at = @At("HEAD"))
    private void slotClicked(Slot slot, int i, int j, ClickType clickType, CallbackInfo ci) {
        if (!MccIslandState.isOnline()) return;
        if (slot == null || !slot.hasItem()) return;
        ItemStack stack = slot.getItem();
        if (CosmeticState.canBeEquipped(stack)) {
            COSMETIC_TYPE type = CosmeticState.getType(stack);
            if (type == null) return;
            Cosmetic cosmeticByType = CosmeticState.getCosmeticByType(type);
            if (cosmeticByType == null) return;

            cosmeticByType.setOriginal(new CosmeticSlot(stack.copy()));
        }
    }

    private void triggerPreviewClicked(int keyCode) {
        if (!MccIslandState.isOnline()) return;
        if (hoveredSlot == null || !hoveredSlot.hasItem()) return;
        InputConstants.Key previewBind = KeyBindingHelper.getBoundKeyOf(minecraft.options.keyPickItem);
        if (keyCode == previewBind.getValue()) {
            ItemStack item = hoveredSlot.getItem();
            if (item.is(Items.GHAST_TEAR) || item.is(Items.AIR)) return;
            if (CosmeticState.canPreviewItem(item))
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

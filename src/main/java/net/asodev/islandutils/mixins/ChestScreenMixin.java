package net.asodev.islandutils.mixins;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.asodev.islandutils.client.IslandutilsClient;
import net.asodev.islandutils.state.COSMETIC_TYPE;
import net.asodev.islandutils.state.CosmeticState;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.client.gui.GuiComponent.blit;

@Mixin(AbstractContainerScreen.class)
public abstract class ChestScreenMixin extends Screen {
    private static final ResourceLocation PREVIEW = new ResourceLocation("island", "textures/preview.png");

    protected ChestScreenMixin(Component component) {
        super(component);
    }

    @Inject(method = "renderSlot", at = @At("TAIL"))
    private void renderSlot(PoseStack poseStack, Slot slot, CallbackInfo ci) {
        if (slot.getItem() == null) return;
        ItemStack slotItem = slot.getItem();

        CompoundTag slotTag = slotItem.getTag();
        if (slotTag == null) return;

        if (itemsMatch(slot.getItem(), CosmeticState.hatSlot) || itemsMatch(slot.getItem(), CosmeticState.accSlot)) {
            this.setBlitOffset(395);
            this.itemRenderer.blitOffset = 105.0F;
            RenderSystem.setShaderTexture(0, PREVIEW);
            blit(poseStack, slot.x-3, slot.y-4, this.getBlitOffset(), 0, 0, 22, 24, 22, 24);
        }
    }

    boolean itemsMatch(ItemStack item, ItemStack compare) {
        ItemStack item1 = item != null ? item : ItemStack.EMPTY;
        ItemStack item2 = compare != null ? compare : ItemStack.EMPTY;

        CompoundTag item1Tag = item1.getTag();
        int item1CMD  = item1Tag == null ? -1 : item1Tag.getInt("CustomModelData");

        CompoundTag item2Tag = item2.getTag();
        int item2CMD  = item2Tag == null ? -1 : item2Tag.getInt("CustomModelData");

        return item1.is(item2.getItem()) && item1CMD == item2CMD;
    }

    @Inject(method = "mouseDragged", at = @At("HEAD"))
    private void mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY, CallbackInfoReturnable<Boolean> cir) {
        CosmeticState.yRot = CosmeticState.yRot - Double.valueOf(deltaX).floatValue();
    }

    @Inject(method = "keyPressed", at = @At("HEAD"))
    private void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        ItemStack item = CosmeticState.getLastHoveredItem();
        COSMETIC_TYPE type = CosmeticState.getLastHoveredItemType();
        if (item == null || type == null) return;

        InputConstants.Key bound = KeyBindingHelper.getBoundKeyOf(IslandutilsClient.previewKeyBind);
        if (keyCode == bound.getValue()) {
            CompoundTag slotTag = item.getTag();
            if (slotTag == null) return;

            CompoundTag hatTag = CosmeticState.hatSlot == null ? null : CosmeticState.hatSlot.getTag();
            CompoundTag accTag = CosmeticState.accSlot == null ? null : CosmeticState.accSlot.getTag();

            int hatCMD  = hatTag == null ? -1 : hatTag.getInt("CustomModelData");
            int accCMD  = accTag == null ? -1 : accTag.getInt("CustomModelData");
            int slotCMD = slotTag.getInt("CustomModelData");

            if (type == COSMETIC_TYPE.HAT) {
                if (slotCMD == hatCMD) CosmeticState.hatSlot = null;
                else CosmeticState.hatSlot = item;
            }
            else if (type == COSMETIC_TYPE.ACCESSORY) {
                if (slotCMD == accCMD) CosmeticState.accSlot = null;
                else CosmeticState.accSlot = item;
            }
        }
    }

    @Inject(method = "onClose", at = @At("TAIL"))
    private void onClose(CallbackInfo ci) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (CosmeticState.prevHatSlot != null) {
            if (player != null) Minecraft.getInstance().player.getInventory().armor.set(3, CosmeticState.prevHatSlot);
        }
        if (CosmeticState.prevAccSlot != null) {
            if (player != null) Minecraft.getInstance().player.getInventory().offhand.set(0, CosmeticState.prevAccSlot);
        }

        CosmeticState.setLastHoveredItem(null);
        CosmeticState.hatSlot = null;
        CosmeticState.accSlot = null;

        CosmeticState.yRot = 180;
    }

}

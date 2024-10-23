package net.asodev.islandutils.mixins.cosmetics;

import com.mojang.blaze3d.platform.InputConstants;
import net.asodev.islandutils.modules.cosmetics.Cosmetic;
import net.asodev.islandutils.modules.cosmetics.CosmeticSlot;
import net.asodev.islandutils.modules.cosmetics.CosmeticState;
import net.asodev.islandutils.modules.cosmetics.CosmeticType;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.state.MccIslandState;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static net.asodev.islandutils.util.Utils.customModelData;


@Mixin(AbstractContainerScreen.class)
public abstract class ChestScreenMixin extends Screen {
    private static final ResourceLocation PREVIEW = ResourceLocation.fromNamespaceAndPath("island", "textures/preview.png");

    @Shadow protected Slot hoveredSlot;

    @Shadow @Final protected AbstractContainerMenu menu;

    @Shadow protected abstract List<Component> getTooltipFromContainerItem(ItemStack itemStack);

    protected ChestScreenMixin(Component component) {
        super(component);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(AbstractContainerMenu abstractContainerMenu, Inventory inventory, Component component, CallbackInfo ci) {
        LocalPlayer player = Minecraft.getInstance().player;
        CosmeticState.hatSlot.setOriginal(new CosmeticSlot(player.getInventory().armor.get(3)));
        CosmeticState.accessorySlot.setOriginal(new CosmeticSlot(player.getInventory().offhand.get(0)));
    }

    @Inject(method = "renderSlot", at = @At("TAIL"))
    private void renderSlot(GuiGraphics guiGraphics, Slot slot, CallbackInfo ci) {
        if (!MccIslandState.isOnline()) return;
        if (!slot.hasItem()) return;

        boolean shouldRender = false;

        if (CosmeticState.hatSlot.preview != null && CosmeticState.hatSlot.preview.matchesSlot(slot)) shouldRender = true;
        else if (CosmeticState.accessorySlot.preview != null && CosmeticState.accessorySlot.preview.matchesSlot(slot)) shouldRender = true;

        guiGraphics.pose().pushPose();
        if (shouldRender) {
            guiGraphics.pose().translate(0.0f, 0.0f, 105f);
            guiGraphics.blitSprite(RenderType::guiTextured, PREVIEW, slot.x-3, slot.y-4, 105, 22, 24);
        }
        guiGraphics.pose().popPose();
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void render(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) {
        if (hoveredSlot != null && hoveredSlot.hasItem()) {
            ItemStack hoveredItem = hoveredSlot.getItem();
            if (IslandOptions.getCosmetics().isShowOnHover()) {
                CosmeticType changedType = setHovered(hoveredItem);
                if (changedType != CosmeticType.HAT) CosmeticState.hatSlot.hover = null;
                if (changedType != CosmeticType.ACCESSORY) CosmeticState.accessorySlot.hover = null;
            }

            if (CosmeticState.isColoredItem(hoveredItem)) {
                Integer color = CosmeticState.getColor(hoveredSlot.getItem());
                if (color != null) {
                    CosmeticState.hoveredColor = color;
                    return;
                }
            }
        } else {
            CosmeticState.hatSlot.hover = null;
            CosmeticState.accessorySlot.hover = null;
        }
        CosmeticState.hoveredColor = null;
    }

    private CosmeticType setHovered(ItemStack item) {
        CosmeticType type = CosmeticState.getType(item);
        if (type == null) return null;
        Cosmetic cosmeticByType = CosmeticState.getCosmeticByType(type);
        if (cosmeticByType == null) return null;
        cosmeticByType.hover = new CosmeticSlot(item, hoveredSlot);
        return type;
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
            CosmeticType type = CosmeticState.getType(stack);
            if (type == null) return;
            Cosmetic cosmeticByType = CosmeticState.getCosmeticByType(type);
            if (cosmeticByType == null) return;

            cosmeticByType.setOriginal(new CosmeticSlot(stack.copy()));
        }
    }

    private void triggerPreviewClicked(int keyCode) {
        if (!MccIslandState.isOnline()) return;
        if (!IslandOptions.getCosmetics().isShowPlayerPreview()) return;
        if (hoveredSlot == null || !hoveredSlot.hasItem()) return;
        InputConstants.Key previewBind = KeyBindingHelper.getBoundKeyOf(minecraft.options.keyPickItem);
        if (keyCode == previewBind.getValue()) {
            ItemStack item = hoveredSlot.getItem();
            if (item.is(Items.GHAST_TEAR) || item.is(Items.AIR)) return;
            setPreview(item);
        }
    }


    @Unique
    private void setPreview(ItemStack item) {
        CosmeticType type = CosmeticState.getType(item);
        int hoverCMD = customModelData(item);
        if (type == CosmeticType.HAT) setOrNotSet(CosmeticState.hatSlot, hoverCMD);
        else if (type == CosmeticType.ACCESSORY) setOrNotSet(CosmeticState.accessorySlot, hoverCMD);
    }

    @Unique
    private void setOrNotSet(Cosmetic cosmetic, int itemCMD) {
        if (cosmetic.preview == null || itemCMD != customModelData(cosmetic.preview.item))
            cosmetic.preview = new CosmeticSlot(hoveredSlot);
        else
            cosmetic.preview = null;
    }

    @Inject(method = "onClose", at = @At("TAIL"))
    private void onClose(CallbackInfo ci) {
        CosmeticState.hatSlot = new Cosmetic(CosmeticType.HAT);
        CosmeticState.accessorySlot = new Cosmetic(CosmeticType.ACCESSORY);

        CosmeticState.inspectingPlayer = null;

        CosmeticState.yRot = 155;
        CosmeticState.xRot = -5;
    }

}

package net.asodev.islandutils.mixins.cosmetics;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.asodev.islandutils.mixins.accessors.WalkAnimStateAccessor;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.state.cosmetics.CosmeticState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

import static net.asodev.islandutils.state.cosmetics.CosmeticState.applyColor;
import static net.asodev.islandutils.state.cosmetics.CosmeticState.isCosmeticMenu;

@Mixin(ContainerScreen.class)
public abstract class UIMixin extends AbstractContainerScreen<ChestMenu> {
    public UIMixin(ChestMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Inject(method = "renderBg", at = @At("TAIL"))
    public void renderBg(PoseStack poseStack, float f, int i, int j, CallbackInfo ci) {
        if (!MccIslandState.isOnline()) return;

        IslandOptions options = IslandOptions.getOptions();
        if (!options.isShowPlayerPreview()) return;
        if (options.isShowOnOnlyCosmeticMenus() && !CosmeticState.isCosmeticMenu(this.menu)) return;

        checkInspect();

        Player player = CosmeticState.getInspectingPlayer();
        Player localPlayer = Minecraft.getInstance().player;
        if (player == null) return;
        if (localPlayer == null) return;

        ItemStack hatSlot;
        ItemStack accSlot;
        if (CosmeticState.inspectingPlayer == null || CosmeticState.inspectingPlayer.getUUID() == Minecraft.getInstance().player.getUUID()) {
            hatSlot = CosmeticState.hatSlot.getContent().getItem(this.menu);
            accSlot = CosmeticState.accessorySlot.getContent().getItem(this.menu);

            if (isCosmeticMenu(this.menu)) {
                applyColor(hatSlot);
                applyColor(accSlot);

                player.getInventory().armor.set(3, hatSlot);
                player.getInventory().offhand.set(0, accSlot);
            }
        } else {
            hatSlot = player.getInventory().armor.get(3);
            accSlot = player.getInventory().offhand.get(0);
        }

        float animPos = player.walkAnimation.position();
        float animSpeed = player.walkAnimation.speed();
        float attackAnim = player.attackAnim;
        WalkAnimStateAccessor walkAnim = (WalkAnimStateAccessor) player.walkAnimation;
        walkAnim.setPosition(0f);
        walkAnim.setSpeed(0f);
        player.attackAnim = 0;

        int size = Double.valueOf(Math.ceil(this.imageHeight / 2.5)).intValue();
        int x = (this.width - this.imageWidth) / 4;
        int y = (this.height / 2) + size;

        this.renderPlayerInInventory(
                x, // x
                y,  // y
                size , // size
                player); // Entity

        walkAnim.setPosition(animPos);
        walkAnim.setSpeed(animPos);
        player.attackAnim = attackAnim;

        // this code is so ugly omfg
        int itemPos = x+(size / 2) - 18;

        y += 8;
        int backgroundColor = 0x60000000;
        fill(poseStack, x-(size / 2) - 2, y, x+(size / 2)+2, y + 19, backgroundColor);
        drawString(poseStack, this.font, CosmeticState.HAT_COMP, x-(size / 2) + 4, y + 6, 16777215 | 255 << 24);
        this.itemRenderer.renderAndDecorateItem(poseStack, this.minecraft.player, hatSlot, itemPos, y+2, x + y * this.imageWidth);

        y += 19 + 4;
        fill(poseStack, x-(size / 2) - 2, y, x+(size / 2)+2, y + 19, backgroundColor);
        drawString(poseStack, this.font, CosmeticState.ACCESSORY_COMP, x-(size / 2) + 4, y + 6, 16777215 | 255 << 24);
        this.itemRenderer.renderAndDecorateItem(poseStack, this.minecraft.player, accSlot, itemPos, y+2, x + y * this.imageWidth);

        if (this.hoveredSlot != null) {
            ItemStack currHover = this.hoveredSlot.getItem();
            if (!currHover.is(Items.GHAST_TEAR) && !CosmeticState.isLockedItem(currHover) && !currHover.is(Items.AIR)) {
                CosmeticState.setHoveredItem(this.hoveredSlot);
            }
        }
    }

    private void renderPlayerInInventory(int x, int y, int size, LivingEntity livingEntity) {
        float yRot = CosmeticState.yRot;
        float xRot = (float)Math.atan(CosmeticState.xRot / 40.0f);;

        PoseStack poseStack = RenderSystem.getModelViewStack();
        poseStack.pushPose();
        poseStack.translate((double)x, (double)y, 1050.0);
        poseStack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        PoseStack poseStack2 = new PoseStack();
        poseStack2.translate(0.0, 0.0, 1000.0);
        poseStack2.scale((float)size, (float)size, (float)size);
        Quaternionf quaternionf = (new Quaternionf()).rotateZ(3.1415927F);
        Quaternionf quaternionf2 = (new Quaternionf()).rotateX(xRot * 20.0F * 0.017453292F);
        quaternionf.mul(quaternionf2);
        poseStack2.mulPose(quaternionf);
        float m = livingEntity.yBodyRot;
        float n = livingEntity.getYRot();
        float o = livingEntity.getXRot();
        float p = livingEntity.yHeadRotO;
        float q = livingEntity.yHeadRot;
        livingEntity.yBodyRot = yRot;
        livingEntity.setYRot(yRot);
        livingEntity.yHeadRot = livingEntity.yBodyRot;
        livingEntity.yHeadRotO = livingEntity.yBodyRot;

        livingEntity.setXRot(-xRot * 20F);

        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        entityRenderDispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> {
            entityRenderDispatcher.render(livingEntity, 0.0, 0.0, 0.0, 0.0F, 1.0F, poseStack2, bufferSource, 15728880);
        });
        bufferSource.endBatch();
        entityRenderDispatcher.setRenderShadow(true);
        livingEntity.yBodyRot = m;
        livingEntity.setYRot(n);
        livingEntity.setXRot(o);
        livingEntity.yHeadRotO = p;
        livingEntity.yHeadRot = q;
        poseStack.popPose();
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
    }

    private void checkInspect() {
        // 11 - Head
        // 9 - Hat
        // 18 - Accessory
        Slot inspectHeadSlot = this.menu.getSlot(11);
        Slot inspectHatSlot = this.menu.getSlot(9);
        boolean isInspectHead = inspectHeadSlot.hasItem() && inspectHeadSlot.getItem().is(Items.PLAYER_HEAD);
        boolean isInspectHat = inspectHeadSlot.hasItem() &&
                (CosmeticState.getType(inspectHatSlot.getItem()) != null) || (inspectHatSlot.getItem().getDisplayName().getString().contains("Hat"));
        if (isInspectHead && isInspectHat) {
            try {
                Player player = null;
                if (CosmeticState.inspectingPlayer == null) {
                    UUID uuid = inspectHeadSlot.getItem().getTag().getCompound("SkullOwner").getUUID("Id");
                    player = Minecraft.getInstance().level.getPlayerByUUID(uuid);
                    CosmeticState.inspectingPlayer = player;
                }
                if (player == null) return;
            } catch (Exception ignored) {}
        }
    }
}

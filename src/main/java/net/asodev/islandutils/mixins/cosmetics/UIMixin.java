package net.asodev.islandutils.mixins.cosmetics;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.asodev.islandutils.mixins.accessors.WalkAnimStateAccessor;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.options.categories.CosmeticsOptions;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.modules.cosmetics.CosmeticState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
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

import static net.asodev.islandutils.modules.cosmetics.CosmeticState.applyColor;
import static net.asodev.islandutils.modules.cosmetics.CosmeticState.isCosmeticMenu;

@Mixin(ContainerScreen.class)
public abstract class UIMixin extends AbstractContainerScreen<ChestMenu> {
    public UIMixin(ChestMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Inject(method = "renderBg", at = @At("TAIL"))
    public void renderBg(GuiGraphics guiGraphics, float f, int i, int j, CallbackInfo ci) {
        if (!MccIslandState.isOnline()) return;

        CosmeticsOptions options = IslandOptions.getCosmetics();
        if (!options.isShowPlayerPreview()) return;
        if (options.isShowOnOnlyCosmeticMenus() && !CosmeticState.isCosmeticMenu(this.menu)) return;

        checkInspect();

        Player player = CosmeticState.getInspectingPlayer();
        Player localPlayer = Minecraft.getInstance().player;
        if (player == null) return;
        if (localPlayer == null) return;

        ItemStack defaultHatSlot = null;
        ItemStack defaultAccSlot = null;

        ItemStack hatSlot;
        ItemStack accSlot;
        if (CosmeticState.inspectingPlayer == null || CosmeticState.inspectingPlayer.getUUID() == Minecraft.getInstance().player.getUUID()) {
            hatSlot = CosmeticState.hatSlot.getContents().getItem(this.menu);
            accSlot = CosmeticState.accessorySlot.getContents().getItem(this.menu);
            if (isCosmeticMenu(this.menu)) {
                applyColor(hatSlot);
                applyColor(accSlot);

                defaultHatSlot = player.getInventory().armor.get(3);
                defaultAccSlot = player.getInventory().offhand.get(0);
                player.getInventory().armor.set(3, hatSlot);
                player.getInventory().offhand.set(0, accSlot);
            }
        } else {
            hatSlot = player.getInventory().armor.get(3);
            accSlot = player.getInventory().offhand.get(0);
        }

        WalkAnimStateAccessor walkAnim = (WalkAnimStateAccessor) player.walkAnimation;
        float animPos = walkAnim.getPosition();
        float animSpeed = walkAnim.getSpeed();
        float animSpeedOld = walkAnim.getSpeedOld();
        float attackAnim = player.attackAnim;

        walkAnim.setPosition(0f);
        walkAnim.setSpeed(0f);
        walkAnim.setSpeedOld(0f);
        player.attackAnim = 0;

        int size = Double.valueOf(Math.ceil(this.imageHeight / 2.5)).intValue();
        int x = (this.width - this.imageWidth) / 4;
        int y = (this.height / 2) + size;

        this.renderPlayerInInventory(
                guiGraphics,
                x, // x
                y,  // y
                size , // size
                player); // Entity

        walkAnim.setPosition(animPos);
        walkAnim.setSpeed(animSpeed);
        walkAnim.setSpeedOld(animSpeedOld);
        player.attackAnim = attackAnim;
        if (defaultHatSlot != null) player.getInventory().armor.set(3, defaultHatSlot);
        if (defaultAccSlot != null) player.getInventory().offhand.set(0, defaultAccSlot);

        // this code is so ugly omfg
        int itemPos = x+(size / 2) - 18;

        y += 8;
        int backgroundColor = 0x60000000;
        guiGraphics.fill(x-(size / 2) - 2, y, x+(size / 2)+2, y + 19, backgroundColor);
        guiGraphics.drawString(this.font, CosmeticState.HAT_COMP, x-(size / 2) + 4, y + 6, 16777215 | 255 << 24);
        guiGraphics.renderItem(this.minecraft.player, hatSlot, itemPos, y+2, x + y * this.imageWidth);

        y += 19 + 4;
        guiGraphics.fill(x-(size / 2) - 2, y, x+(size / 2)+2, y + 19, backgroundColor);
        guiGraphics.drawString(this.font, CosmeticState.ACCESSORY_COMP, x-(size / 2) + 4, y + 6, 16777215 | 255 << 24);
        guiGraphics.renderItem(this.minecraft.player, accSlot, itemPos, y+2, x + y * this.imageWidth);
    }

    // don't ask what this code does
    // the answer is "it renders the player, it works, no touch."
    private void renderPlayerInInventory(GuiGraphics guiGraphics, int x, int y, int size, LivingEntity livingEntity) {
        float yRot = CosmeticState.yRot;
        float xRot = (float)Math.atan(CosmeticState.xRot / 40.0f);;

        InventoryScreen.renderEntityInInventoryFollowsMouse(
                guiGraphics,
                x, y,
                x, y,
                size,
                0.0625f,
                xRot,
                yRot,
                livingEntity
        );
    }

    private void checkInspect() {
        // TODO: Readd inspect menu support
    }
}

package net.asodev.islandutils.mixins.cosmetics;

import net.asodev.islandutils.mixins.accessors.WalkAnimStateAccessor;
import net.asodev.islandutils.modules.cosmetics.CosmeticState;
import net.asodev.islandutils.modules.cosmetics.CosmeticType;
import net.asodev.islandutils.modules.cosmetics.CosmeticUI;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.options.categories.CosmeticsOptions;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.util.FontUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.asodev.islandutils.modules.cosmetics.CosmeticState.applyColor;

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
        boolean isCosmeticMenu = false;
        if (options.isShowOnOnlyCosmeticMenus() && !(isCosmeticMenu = CosmeticState.isCosmeticMenu(this))) return;

        checkInspect();

        Player player = CosmeticState.getInspectingPlayer();
        Player localPlayer = Minecraft.getInstance().player;
        if (player == null) return;
        if (localPlayer == null) return;

        ItemStack defaultMainSlot = null;
        ItemStack defaultHatSlot = null;
        ItemStack defaultAccSlot = null;

        ItemStack hatSlot;
        ItemStack accSlot;

        Inventory playerInventory = player.getInventory();
        if (CosmeticState.inspectingPlayer == null || CosmeticState.inspectingPlayer.getUUID() == Minecraft.getInstance().player.getUUID()) {
            hatSlot = CosmeticState.hatSlot.getContents().getItem(this.menu);
            accSlot = CosmeticState.accessorySlot.getContents().getItem(this.menu);
            ItemStack mainSlot = CosmeticState.mainHandSlot.getContents().getItem(this.menu);
            if (isCosmeticMenu) {
                applyColor(hatSlot);
                applyColor(accSlot);

                defaultHatSlot = CosmeticType.HAT.getItem(playerInventory);
                playerInventory.setItem(CosmeticType.HAT.getIndex(), hatSlot);

                defaultAccSlot = CosmeticType.ACCESSORY.getItem(playerInventory);
                playerInventory.setItem(CosmeticType.ACCESSORY.getIndex(), accSlot);

                defaultMainSlot = player.getMainHandItem();
                player.setItemSlot(EquipmentSlot.MAINHAND, mainSlot);
            }
        } else {
            hatSlot = CosmeticType.HAT.getItem(playerInventory);
            accSlot = CosmeticType.ACCESSORY.getItem(playerInventory);
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
        int bounds = this.imageHeight;
        int x = (this.width - this.imageWidth) / 4;
        int y = (this.height / 2) + size;

        CosmeticUI.renderPlayerInInventory(
                guiGraphics,
                x - bounds, // x0
                (this.height / 2) - bounds, // y0
                x + bounds, // x1
                (this.height / 2) + bounds, // y1
                size, // size
                player); // Entity

        walkAnim.setPosition(animPos);
        walkAnim.setSpeed(animSpeed);
        walkAnim.setSpeedOld(animSpeedOld);
        player.attackAnim = attackAnim;
        if (defaultHatSlot != null) playerInventory.setItem(CosmeticType.HAT.getIndex(), defaultHatSlot);
        if (defaultAccSlot != null) playerInventory.setItem(CosmeticType.ACCESSORY.getIndex(), defaultAccSlot);
        if (defaultMainSlot != null) player.setItemSlot(EquipmentSlot.MAINHAND, defaultMainSlot);

        // this code is so ugly omfg
        int itemPos = x+(size / 2) - 18;

        y += 8;
        int backgroundColor = 0x60000000;
        guiGraphics.fill(x-(size / 2) - 2, y, x+(size / 2)+2, y + 19, backgroundColor);
        guiGraphics.drawString(this.font, FontUtils.TOOLTIP_HAT, x-(size / 2) + 4, y + 6, 16777215 | 255 << 24);
        guiGraphics.renderItem(this.minecraft.player, hatSlot, itemPos, y+2, x + y * this.imageWidth);

        y += 19 + 4;
        guiGraphics.fill(x-(size / 2) - 2, y, x+(size / 2)+2, y + 19, backgroundColor);
        guiGraphics.drawString(this.font, FontUtils.TOOLTIP_ACCESSORY, x-(size / 2) + 4, y + 6, 16777215 | 255 << 24);
        guiGraphics.renderItem(this.minecraft.player, accSlot, itemPos, y+2, x + y * this.imageWidth);
    }

    private void checkInspect() {
        // TODO: Readd inspect menu support
    }
}

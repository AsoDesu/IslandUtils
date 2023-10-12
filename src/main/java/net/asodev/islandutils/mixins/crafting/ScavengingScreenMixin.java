package net.asodev.islandutils.mixins.crafting;

import net.asodev.islandutils.modules.scavenging.Scavenging;
import net.asodev.islandutils.modules.scavenging.ScavengingTotalList;
import net.asodev.islandutils.state.MccIslandState;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ContainerScreen.class)
public abstract class ScavengingScreenMixin extends AbstractContainerScreen<ChestMenu> {
    public ScavengingScreenMixin(ChestMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
    }

    @Inject(method = "renderBg", at = @At("TAIL"))
    public void renderBg(GuiGraphics guiGraphics, float f, int i, int j, CallbackInfo ci) {
        if (!MccIslandState.isOnline() || !Scavenging.isScavengingMenuOrDisabled(this)) return;

        ScavengingTotalList silverTotal = Scavenging.getSilverTotal(this.menu);
        Scavenging.renderSilverTotal(silverTotal, guiGraphics);
    }
}

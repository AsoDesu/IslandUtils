package net.asodev.islandutils.mixins.ui;

import net.asodev.islandutils.state.Game;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.util.Utils;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public abstract class SlotMixin {

    @Shadow
    public abstract boolean hasItem();

    @Shadow
    public abstract ItemStack getItem();

    @Inject(method = "isHighlightable", at = @At("HEAD"), cancellable = true)
    private void isHighlightable(CallbackInfoReturnable<Boolean> cir) {
        if (MccIslandState.isOnline() && MccIslandState.getGame().equals(Game.HUB)) {
            Identifier itemID = Utils.getCustomItemID(this.getItem());
            if (itemID == null) {
                cir.setReturnValue(this.hasItem());
            } else if (itemID.getPath().equals("island_interface.generic.blank")) {
                cir.setReturnValue(false);
            }
        }
    }
}
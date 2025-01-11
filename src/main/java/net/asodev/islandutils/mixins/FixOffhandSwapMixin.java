package net.asodev.islandutils.mixins;

import net.asodev.islandutils.state.MccIslandState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorItem.class)
public abstract class FixOffhandSwapMixin {
    @Shadow
    public abstract EquipmentSlot getEquipmentSlot();

    @Inject(method = "use", at = @At(value = "HEAD"), cancellable = true)
    private void use(Level level, Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> ci) {
        if (!MccIslandState.isOnline()) return;
        if (getEquipmentSlot().getType() != EquipmentSlot.Type.HUMANOID_ARMOR) {
            ItemStack itemStack = player.getItemInHand(interactionHand);
            ci.setReturnValue(InteractionResultHolder.pass(itemStack));
        }
    }

}

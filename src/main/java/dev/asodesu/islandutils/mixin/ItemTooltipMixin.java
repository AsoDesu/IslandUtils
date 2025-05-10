package dev.asodesu.islandutils.mixin;

import dev.asodesu.islandutils.api.extentions.DebugExtKt;
import dev.asodesu.islandutils.api.extentions.MinecraftExtKt;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class ItemTooltipMixin {

    @Inject(
            method = "addDetailsToTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/TooltipFlag;isAdvanced()Z",
                    shift = At.Shift.AFTER
            )
    )
    private void addDetailsToTooltip(Item.TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, @Nullable Player player, TooltipFlag tooltipFlag, Consumer<Component> consumer, CallbackInfo ci) {
        if (!MinecraftExtKt.isOnline()) return;

        ItemStack item = (ItemStack) (Object) this;
        DebugExtKt.addDebugTooltip(item, consumer);
    }

}

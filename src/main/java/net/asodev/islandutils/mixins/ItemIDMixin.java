package net.asodev.islandutils.mixins;

import com.mojang.blaze3d.platform.InputConstants;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.state.MccIslandState;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemIDMixin {

    @Shadow public abstract boolean hasTag();

    @Shadow public abstract @Nullable CompoundTag getTagElement(String string);

    @Inject(
            method = "getTooltipLines",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/core/DefaultedRegistry;getKey(Ljava/lang/Object;)Lnet/minecraft/resources/ResourceLocation;",
                    shift = At.Shift.AFTER
            ),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private void addTooltipLines(@Nullable Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir, List<Component> list) {
        if (!MccIslandState.isOnline() || !IslandOptions.getMisc().isDebugMode()) return;
        if (!InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_LCONTROL)) return;
        if (!this.hasTag()) return;

        CompoundTag publicBukkitValues = this.getTagElement("PublicBukkitValues");
        if (publicBukkitValues == null) return;
        String customItemId = publicBukkitValues.getString("mcc:custom_item_id");
        if (customItemId.equals("")) return;
        list.add(Component.literal(customItemId).withStyle(ChatFormatting.AQUA));
    }

}

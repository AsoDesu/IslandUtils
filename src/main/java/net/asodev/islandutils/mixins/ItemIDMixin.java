package net.asodev.islandutils.mixins;

import com.mojang.blaze3d.platform.InputConstants;
import net.asodev.islandutils.mixins.accessors.ContainerScreenAccessor;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.state.MccIslandState;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemIDMixin implements DataComponentHolder {

    @Inject(
            method = "getTooltipLines",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/core/DefaultedRegistry;getKey(Ljava/lang/Object;)Lnet/minecraft/resources/ResourceLocation;",
                    shift = At.Shift.AFTER
            ),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private void addTooltipLines(Item.TooltipContext tooltipContext, @Nullable Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir, List list) {
        if (!MccIslandState.isOnline() || !IslandOptions.getMisc().isDebugMode()) return;
        if (!InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_LCONTROL)) return;

        MutableComponent toAppend = Component.empty();
        if (this.has(DataComponents.CUSTOM_DATA)) tryAddCustomItemID(toAppend); // Append MCCI Custom Item ID
        if (Minecraft.getInstance().screen != null) tryAddSlotNumber(toAppend); // Append Slot Number

        if (!toAppend.getSiblings().isEmpty()) { // Add to tooltip lines if not empty
            list.add(toAppend);
        }
    }

    @Unique
    private void tryAddCustomItemID(MutableComponent toAppend) {
        CustomData customData = this.get(DataComponents.CUSTOM_DATA);
        if (customData == null) return;
        CompoundTag tag = customData.getUnsafe();
        CompoundTag publicBukkitValues = tag.getCompound("PublicBukkitValues");
        String customItemId = publicBukkitValues.getString("mcc:custom_item_id");
        if (customItemId.isEmpty()) return;
        toAppend.append(Component.literal(customItemId).withStyle(ChatFormatting.AQUA));
    }

    @Unique
    private void tryAddSlotNumber(MutableComponent toAppend) {
        ItemStack me = (ItemStack)(Object)this;
        Screen screen = Minecraft.getInstance().screen;
        if (!(screen instanceof AbstractContainerScreen<?> containerScreen)) return;
        Slot hoveredSlot = ((ContainerScreenAccessor) containerScreen).getHoveredSlot();

        if (!hoveredSlot.hasItem()) return;
        if (hoveredSlot.getItem() != me) return;
        Component component = Component.literal(" (" + hoveredSlot.getContainerSlot() + ")").withStyle(ChatFormatting.GRAY);
        toAppend.append(component);
    }

}

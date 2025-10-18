package net.asodev.islandutils.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.platform.InputConstants;
import net.asodev.islandutils.mixins.accessors.ContainerScreenAccessor;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.util.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemLore;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mixin(ItemStack.class)
public abstract class ItemIDMixin implements DataComponentHolder {

    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger("IslandUtils");

    @Unique
    private int itemPrintCounter = -1;

    @Inject(
            method = "getTooltipLines",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;addDetailsToTooltip(Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/item/component/TooltipDisplay;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/TooltipFlag;Ljava/util/function/Consumer;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void addTooltipLines(Item.TooltipContext tooltipContext, @Nullable Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir, @Local List<Component> list) {
        if (!MccIslandState.isOnline() || !IslandOptions.getMisc().isDebugMode()) return;

        var window = Minecraft.getInstance().getWindow().getWindow();

        var itemPrintHotkeyHeld = InputConstants.isKeyDown(window, InputConstants.KEY_F3) && InputConstants.isKeyDown(window, InputConstants.KEY_Z);
        itemPrintCounter = (itemPrintHotkeyHeld) ? itemPrintCounter + 1 : -1;
        if (itemPrintCounter == 0) printItemInfo();

        if (!InputConstants.isKeyDown(window, InputConstants.KEY_LCONTROL)) return;

        MutableComponent toAppend = Component.empty();
        if (this.has(DataComponents.CUSTOM_DATA)) tryAddCustomItemID(toAppend); // Append MCCI Custom Item ID
        if (Minecraft.getInstance().screen != null) tryAddSlotNumber(toAppend); // Append Slot Number

        if (!toAppend.getSiblings().isEmpty()) { // Add to tooltip lines if not empty
            list.add(toAppend);
        }
    }

    @Unique
    private void tryAddCustomItemID(MutableComponent toAppend) {
        ResourceLocation customItemID = Utils.getCustomItemID((ItemStack) (Object) this);
        if (customItemID == null) return;
        toAppend.append(Component.literal(customItemID.toString()).withStyle(ChatFormatting.AQUA));
    }

    @Unique
    private Optional<Slot> getHoveredSlot() {
        ItemStack me = (ItemStack) (Object) this;
        Screen screen = Minecraft.getInstance().screen;
        if (screen == null) return Optional.empty();
        if (!(screen instanceof AbstractContainerScreen<?> containerScreen)) return Optional.empty();
        Slot hoveredSlot = ((ContainerScreenAccessor) containerScreen).getHoveredSlot();

        if (hoveredSlot == null || !hoveredSlot.hasItem()) return Optional.empty();
        if (hoveredSlot.getItem() != me) return Optional.empty();
        return Optional.of(hoveredSlot);
    }

    @Unique
    private void tryAddSlotNumber(MutableComponent toAppend) {
        getHoveredSlot().ifPresent(hoveredSlot -> {
            Component component = Component.literal(" (" + hoveredSlot.getContainerSlot() + ")").withStyle(ChatFormatting.GRAY);
            toAppend.append(component);
        });
    }

    @Unique
    private void printItemInfo() {
        getHoveredSlot().ifPresent(hoveredSlot -> {
            LOGGER.info("-----");
            LOGGER.info("Printing components of {}", hoveredSlot.getItem().getItem().getDescriptionId());
            LOGGER.info("-----");
            hoveredSlot.getItem().getComponents().forEach(component -> {
                if (component.value() instanceof Component) {
                    LOGGER.info("{}=>\"{}\"", component.type(), ((Component) component.value()).getString());
                } else if (component.value() instanceof ItemLore) {
                    var lines = ((ItemLore) component.value()).lines();
                    var lineStrings = lines.stream().map(Component::getString).map(StringEscapeUtils::escapeJava);
                    var fullString = lineStrings.collect(Collectors.joining("\\n"));
                    LOGGER.info("{}=>\"{}\"", component.type(), fullString);
                } else {
                    LOGGER.info(component.toString());
                }
            });
            LOGGER.info("-----");
        });
    }

}

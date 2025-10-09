package net.asodev.islandutils.modules.cosmetics;

import net.asodev.islandutils.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CosmeticState {

    @Nullable
    public static Player inspectingPlayer;
    public static float yRot = 155;
    public static float xRot = -5;

    public static Cosmetic hatSlot = new Cosmetic(CosmeticType.HAT);
    public static Cosmetic accessorySlot = new Cosmetic(CosmeticType.ACCESSORY);
    public static Cosmetic mainHandSlot = new Cosmetic(CosmeticType.MAIN_HAND);
    @Nullable
    public static Integer hoveredColor;

    public static Cosmetic getCosmeticByType(CosmeticType type) {
        switch (type) {
            case HAT -> {
                return hatSlot;
            }
            case ACCESSORY -> {
                return accessorySlot;
            }
            case MAIN_HAND -> {
                return mainHandSlot;
            }
        }
        return null;
    }

    public static Player getInspectingPlayer() {
        return (inspectingPlayer == null) ? Minecraft.getInstance().player : inspectingPlayer;
    }

    public static boolean canBeEquipped(ItemStack stack) {
        List<Component> lores = Utils.getTooltipLines(stack);
        if (lores == null) return false;
        return lores.stream().anyMatch(p -> p.getString().contains("Left-Click to Equip"));
    }

    public static boolean isColoredItem(ItemStack item) {
        if (!item.is(Items.LEATHER_HORSE_ARMOR)) return false;
        ResourceLocation customItemID = Utils.getCustomItemID(item);
        if (customItemID == null) return false;
        return customItemID.getPath().equals("island_interface.misc.color");
    }

    public static Integer getColor(ItemStack itemStack) {
        DyedItemColor dyedItemColor = itemStack.get(DataComponents.DYED_COLOR);
        return dyedItemColor != null ? dyedItemColor.rgb() : null;
    }

    public static ItemStack applyColor(ItemStack itemStack) {
        if (hoveredColor == null) return itemStack;
        DyedItemColor itemColor = new DyedItemColor(hoveredColor);
        itemStack.set(DataComponents.DYED_COLOR, itemColor);
        return itemStack;
    }

    public static CosmeticType getType(ItemStack item) {
        ResourceLocation itemId = Utils.getCustomItemID(item);
        if (itemId == null) return null;
        String path = itemId.getPath();
        if (path.endsWith(".icon_empty") || path.endsWith(".icon")) return null;
        if (path.contains("hat.") || path.contains("hair.")) return CosmeticType.HAT;
        if (path.contains("accessory.")) return CosmeticType.ACCESSORY;
        if (path.startsWith("island_lobby.fishing.rods")) return CosmeticType.MAIN_HAND;
        return null;
    }

    public static boolean isCosmeticMenu(AbstractContainerScreen<ChestMenu> screen) {
        if (screen.getTitle().getString().contains("MY PROFILE")) return true;

        ChestMenu menu = screen.getMenu();
        List<ItemStack> slots = new ArrayList<>(menu.slots.stream().map(Slot::getItem).toList());
        slots.add(menu.getCarried());
        for (ItemStack slot : slots) {
            CosmeticType type = CosmeticState.getType(slot);
            if (type != null) return true;
        }
        return false;
    }

    public static boolean itemsMatch(ItemStack item, ItemStack compare) {
        ItemStack item1 = item != null ? item : ItemStack.EMPTY;
        ItemStack item2 = compare != null ? compare : ItemStack.EMPTY;

        ResourceLocation item1ID = Utils.getCustomItemID(item1);
        ResourceLocation item2ID = Utils.getCustomItemID(item2);

        return Objects.equals(item1ID, item2ID);
    }

}

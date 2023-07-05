package net.asodev.islandutils.state.cosmetics;

import net.asodev.islandutils.state.COSMETIC_TYPE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.world.item.DyeableLeatherItem.TAG_COLOR;
import static net.minecraft.world.item.DyeableLeatherItem.TAG_DISPLAY;

public class CosmeticState {

    public static final ResourceLocation MCC_ICONS = new ResourceLocation("mcc", "icon");
    public static Component HAIR_COMP = Component.literal("\uE0E7").setStyle(Style.EMPTY.withFont(MCC_ICONS));
    public static Component HAT_COMP = Component.literal("\uE0E8").setStyle(Style.EMPTY.withFont(MCC_ICONS));
    public static Component ACCESSORY_COMP = Component.literal("\uE0DA").setStyle(Style.EMPTY.withFont(MCC_ICONS));

    @Nullable public static Player inspectingPlayer;
    public static float yRot = 155;
    public static float xRot = -5;

    public static Cosmetic hatSlot = new Cosmetic(COSMETIC_TYPE.HAT);
    public static Cosmetic accessorySlot = new Cosmetic(COSMETIC_TYPE.ACCESSORY);
    @Nullable public static Integer hoveredColor;

    public static Cosmetic getCosmeticByType(COSMETIC_TYPE type) {
        switch (type) {
            case HAT -> { return hatSlot; }
            case ACCESSORY -> { return accessorySlot; }
        }
        return null;
    }

    public static Player getInspectingPlayer() {
        return (inspectingPlayer == null) ? Minecraft.getInstance().player : inspectingPlayer;
    }

    // Locked items have a lore called "Right-Click to preview"
    public static boolean canPreviewItem(ItemStack item) {
        List<Component> lores = getLores(item);
        if (lores == null) return false;
        return !isLoreLockedItem(lores);
    }
    public static boolean isLoreLockedItem(List<Component> lores) {
        return lores.stream().anyMatch(p -> p.getString().contains("Right-Click to preview"));
    }

    public static boolean canBeEquipped(ItemStack stack) {
        List<Component> lores = getLores(stack);
        if (lores == null) return false;
        return lores.stream().anyMatch(p -> p.getString().contains("Left-Click to Equip"));
    }

    // Color items do not have lore
    public static boolean isColoredItem(ItemStack item) {
        if (!item.is(Items.LEATHER_HORSE_ARMOR)) return false;
        CompoundTag displayTag = item.getTagElement("display");
        if (displayTag == null) return false;
        return !displayTag.getAllKeys().contains("Lore");
    }

    public static Integer getColor(ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getTagElement(TAG_DISPLAY);
        if (compoundTag != null && compoundTag.contains(TAG_COLOR, 99)) {
            return compoundTag.getInt(TAG_COLOR);
        }
        return null;
    }
    public static ItemStack applyColor(ItemStack itemStack) {
        if (hoveredColor == null) return itemStack;
        CompoundTag compoundTag = itemStack.getTagElement(TAG_DISPLAY);
        if (compoundTag != null) {
            compoundTag.putInt(TAG_COLOR, hoveredColor);
        }
        return itemStack;
    }

    public static COSMETIC_TYPE getType(ItemStack item) {
        return getType(getLores(item));
    }
    public static COSMETIC_TYPE getType(List<Component> lores) {
        if (lores == null) return null;
        if (lores.size() > 1) {
            Component line2 = lores.get(1);
            for (Component c : line2.toFlatList()) {
                if (c.contains(HAT_COMP) || c.contains(HAIR_COMP)) { return COSMETIC_TYPE.HAT;  }
                if (c.contains(ACCESSORY_COMP)) { return COSMETIC_TYPE.ACCESSORY; }
            }
        }
        return null;
    }
    private static List<Component> getLores(ItemStack item) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return null;
        return item.getTooltipLines(player, TooltipFlag.Default.NORMAL);
    }

    public static boolean isCosmeticMenu(ChestMenu menu) {
        List<ItemStack> slots = new ArrayList<>(menu.slots.stream().map(Slot::getItem).toList());
        slots.add(menu.getCarried());
        for (ItemStack slot : slots){
            COSMETIC_TYPE type = CosmeticState.getType(slot);
            if (type != null) return true;
        }
        return false;
    }

    public static boolean itemsMatch(ItemStack item, ItemStack compare) {
        ItemStack item1 = item != null ? item : ItemStack.EMPTY;
        ItemStack item2 = compare != null ? compare : ItemStack.EMPTY;

        int item1CMD = customModelData(item1);
        int item2CMD = customModelData(item2);

        return item1.is(item2.getItem()) && item1CMD == item2CMD;
    }

    public static int customModelData(ItemStack item) {
        CompoundTag itemTag = item.getTag();
        return itemTag == null ? -1 : itemTag.getInt("CustomModelData");
    }

}

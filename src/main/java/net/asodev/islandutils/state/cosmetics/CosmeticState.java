package net.asodev.islandutils.state.cosmetics;

import net.asodev.islandutils.state.COSMETIC_TYPE;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.data.models.blockstates.MultiPartGenerator;
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
import java.util.concurrent.atomic.AtomicReference;

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

    public static void setHoveredItem(Slot hover) {
        COSMETIC_TYPE type = getType(hover.getItem());

        if (type == COSMETIC_TYPE.HAT) hatSlot.hovering = new CosmeticSlot(hover);
        else if (type == COSMETIC_TYPE.ACCESSORY) accessorySlot.hovering = new CosmeticSlot(hover);
    }

    public static Player getInspectingPlayer() {
        return (inspectingPlayer == null) ? Minecraft.getInstance().player : inspectingPlayer;
    }

    public static boolean isUnlocked(ItemStack item) {
        if (!item.is(Items.POPPED_CHORUS_FRUIT)) return true;
        boolean out = true;
        return out;
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
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return null;
        List<Component> lores = item.getTooltipLines(player, TooltipFlag.Default.NORMAL);
        if (lores.size() > 1) {
            Component line2 = lores.get(1);
            for (Component c : line2.toFlatList()) {
                if (c.contains(HAT_COMP) || c.contains(HAIR_COMP)) { return COSMETIC_TYPE.HAT;  }
                if (c.contains(ACCESSORY_COMP)) { return COSMETIC_TYPE.ACCESSORY; }
            }
        }
        return null;
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

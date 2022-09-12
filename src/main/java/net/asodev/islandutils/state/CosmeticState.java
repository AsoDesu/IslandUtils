package net.asodev.islandutils.state;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class CosmeticState {

    public static final Component HAT_COMP = Component.literal("\uE0E8").setStyle(Style.EMPTY.withFont(new ResourceLocation("mcc", "icon")));
    public static final Component ACCESSORY_COMP = Component.literal("\uE0DA").setStyle(Style.EMPTY.withFont(new ResourceLocation("mcc", "icon")));

    private static ItemStack lastHoveredItem;
    private static COSMETIC_TYPE lastHoveredItemType;

    @Nullable public static ItemStack prevHatSlot;
    @Nullable public static ItemStack prevAccSlot;

    @Nullable public static ItemStack hatSlot;
    @Nullable public static ItemStack accSlot;

    public static float yRot = 180;

    public static COSMETIC_TYPE getLastHoveredItemType() {
        return lastHoveredItemType;
    }
    private static void setLastHoveredItemType(COSMETIC_TYPE lastHoveredItemType) {
        CosmeticState.lastHoveredItemType = lastHoveredItemType;
    }

    public static ItemStack getLastHoveredItem() {
        return lastHoveredItem;
    }
    public static void setLastHoveredItem(ItemStack item) {
        if (item == null) { CosmeticState.lastHoveredItem = null; CosmeticState.lastHoveredItemType = null; return; }

        COSMETIC_TYPE type = getType(item);
        if (type == null) return;
        setLastHoveredItemType(type);
        CosmeticState.lastHoveredItem = item;
    }

    public static COSMETIC_TYPE getType(ItemStack item) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return null;
        List<Component> lores = item.getTooltipLines(player, TooltipFlag.Default.NORMAL);
        if (lores.size() > 1) {
            AtomicReference<COSMETIC_TYPE> type = new AtomicReference<>();
            Component line2 = lores.get(1);
            line2.toFlatList().forEach(c -> {
                if (c.contains(HAT_COMP)) { type.set(COSMETIC_TYPE.HAT); }
                if (c.contains(ACCESSORY_COMP)) { type.set(COSMETIC_TYPE.ACCESSORY); }
            });
           return type.get();
        }
        return null;
    }

}

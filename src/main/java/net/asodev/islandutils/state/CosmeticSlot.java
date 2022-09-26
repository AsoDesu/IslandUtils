package net.asodev.islandutils.state;

import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import static net.asodev.islandutils.state.CosmeticState.itemsMatch;

public class CosmeticSlot {

    public ItemStack item;
    @Nullable public Slot slot;

    public CosmeticSlot(Slot slot) {
        this(slot.getItem(), slot);
    }

    public CosmeticSlot(ItemStack item) {
        this(item, null);
    }

    public CosmeticSlot(ItemStack item, @Nullable Slot slot) {
        this.item = item;
        this.slot = slot;
    }

    public ItemStack getItem(@Nullable ChestMenu menu) {
        if (menu == null || slot == null) return item;
        Slot menuSlot = menu.getSlot(slot.index);
        if (menuSlot == null || !menuSlot.hasItem() || menuSlot.getItem().is(Items.AIR)) return item;
        ItemStack menuItem = menuSlot.getItem();

        if (!itemsMatch(menuItem, item)) return item;
        else return menuItem;
    }
}

package net.asodev.islandutils.modules.cosmetics;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public enum CosmeticType {

    HAT(EquipmentSlot.HEAD, EquipmentSlot.HEAD.getIndex(36)),
    ACCESSORY(EquipmentSlot.OFFHAND, 40),
    MAIN_HAND(EquipmentSlot.MAINHAND, -1);

    EquipmentSlot slot;
    int index;

    CosmeticType(EquipmentSlot slot, int index) {
        this.slot = slot;
        this.index = index;
    }

    public ItemStack getItem(Inventory playerInventory) {
        if (this.index == -1) return playerInventory.getSelectedItem();
        return playerInventory.getItem(this.index);
    }

    public EquipmentSlot getSlot() {
        return slot;
    }

    public int getIndex() {
        return index;
    }
}

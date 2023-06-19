package net.asodev.islandutils.state.cosmetics;

import net.asodev.islandutils.state.COSMETIC_TYPE;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class Cosmetic {

    @Nullable public CosmeticSlot content;

    public COSMETIC_TYPE type;

    public Cosmetic(COSMETIC_TYPE type) {
        this.type = type;
    }

    public CosmeticSlot getContent() {
        if (content != null) return content;
        return new CosmeticSlot(ItemStack.EMPTY, null);
    }
}

package net.asodev.islandutils.state.cosmetics;

import net.asodev.islandutils.state.COSMETIC_TYPE;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class Cosmetic {

    @Nullable private CosmeticSlot original;
    @Nullable public CosmeticSlot preview;

    public COSMETIC_TYPE type;

    public void setOriginal(CosmeticSlot original) {
        this.original = original;
    }

    public Cosmetic(COSMETIC_TYPE type) {
        this.type = type;
    }

    public CosmeticSlot getContents() {
        if (preview != null) return preview;
        if (original != null) return original;
        return new CosmeticSlot(ItemStack.EMPTY, null);
    }
}

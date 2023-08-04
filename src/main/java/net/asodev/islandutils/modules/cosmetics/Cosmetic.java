package net.asodev.islandutils.modules.cosmetics;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class Cosmetic {

    @Nullable public CosmeticSlot hover;
    @Nullable private CosmeticSlot original;
    @Nullable public CosmeticSlot preview;

    public CosmeticType type;

    public void setOriginal(CosmeticSlot original) {
        this.original = original;
    }

    public Cosmetic(CosmeticType type) {
        this.type = type;
    }

    public CosmeticSlot getContents() {
        if (hover != null) return hover;
        if (preview != null) return preview;
        if (original != null) return original;
        return new CosmeticSlot(ItemStack.EMPTY, null);
    }
}

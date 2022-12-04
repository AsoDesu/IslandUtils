package net.asodev.islandutils.state.cosmetics;

import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.state.COSMETIC_TYPE;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class Cosmetic {

    @Nullable public CosmeticSlot set;
    @Nullable public CosmeticSlot hovering;
    @Nullable public CosmeticSlot preview;

    public COSMETIC_TYPE type;

    public Cosmetic(COSMETIC_TYPE type) {
        this.type = type;
    }

    public CosmeticSlot getContent() {
        IslandOptions options = IslandOptions.getOptions();
        if (preview != null) return preview;
        if (options.isShowOnHover() && hovering != null) return hovering;
        if (set != null) return set;
        return new CosmeticSlot(ItemStack.EMPTY, null);
    }
}

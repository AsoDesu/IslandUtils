package net.asodev.islandutils.state.splits;

import dev.isxander.yacl3.api.NameableEnum;
import net.minecraft.network.chat.Component;

public enum SplitType implements NameableEnum {
    BEST,
    AVG;


    @Override
    public Component getDisplayName() {
        return Component.translatable("islandutils.splittype." + this.name().toLowerCase());
    }
}

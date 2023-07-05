package net.asodev.islandutils.mixins.accessors;

import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerTabOverlay.class)
public interface TabListAccessor {

    @Accessor("header")
    Component getHeader();

}

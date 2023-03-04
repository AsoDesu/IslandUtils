package net.asodev.islandutils.mixins.accessors;

import com.mojang.blaze3d.audio.Listener;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SoundEngine.class)
public interface SoundEngineAccessor {

    @Accessor("listener")
    Listener getListener();

}

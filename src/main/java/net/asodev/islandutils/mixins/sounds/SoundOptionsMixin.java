package net.asodev.islandutils.mixins.sounds;

import net.asodev.islandutils.options.IslandSoundCategories;
import net.asodev.islandutils.state.MccIslandState;
import net.minecraft.client.gui.screens.options.SoundOptionsScreen;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SoundOptionsScreen.class)
public class SoundOptionsMixin {

    @Redirect(method = "getAllSoundOptionsExceptMaster",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/sounds/SoundSource;values()[Lnet/minecraft/sounds/SoundSource;"))
    private SoundSource[] values() {
        if (!MccIslandState.isOnline()) {
            return IslandSoundCategories.before;
        }
        return SoundSource.values();
    }

}

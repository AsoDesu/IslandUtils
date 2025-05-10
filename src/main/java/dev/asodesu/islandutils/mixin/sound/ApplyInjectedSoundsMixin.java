package dev.asodesu.islandutils.mixin.sound;

import dev.asodesu.islandutils.api.music.resources.SoundInjector;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundManager.class)
public class ApplyInjectedSoundsMixin {

    @Inject(
            method = "apply(Lnet/minecraft/client/sounds/SoundManager$Preparations;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/sounds/SoundManager$Preparations;apply(Ljava/util/Map;Ljava/util/Map;Lnet/minecraft/client/sounds/SoundEngine;)V",
                    shift = At.Shift.AFTER
            )
    )
    public void applyResourcePackChanges(SoundManager.Preparations preparations, ResourceManager resourceManager, ProfilerFiller profilerFiller, CallbackInfo ci) {
        SoundInjector.INSTANCE.apply();
    }

}

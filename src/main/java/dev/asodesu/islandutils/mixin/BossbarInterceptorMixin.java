package dev.asodesu.islandutils.mixin;

import dev.asodesu.islandutils.api.events.bossbar.BossbarEvents;
import dev.asodesu.islandutils.api.extentions.MinecraftExtKt;
import net.minecraft.network.chat.Component;
import net.minecraft.world.BossEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(BossEvent.class)
public class BossbarInterceptorMixin {

    @Shadow
    @Final
    private UUID id;

    @Inject(method = "setName", at = @At("HEAD"))
    private void setName(Component component, CallbackInfo ci) {
        if (!MinecraftExtKt.isOnline()) return;
        BossbarEvents.INSTANCE.getBOSSBAR_CONTENTS_UPDATE().invoker().onBossbarContents(this.id, component);
    }

}

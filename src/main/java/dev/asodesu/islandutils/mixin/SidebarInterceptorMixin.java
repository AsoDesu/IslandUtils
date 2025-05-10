package dev.asodesu.islandutils.mixin;

import dev.asodesu.islandutils.api.events.sidebar.SidebarEvents;
import dev.asodesu.islandutils.api.extentions.MinecraftExtKt;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.Score;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Score.class)
public class SidebarInterceptorMixin {

    @Inject(method = "display(Lnet/minecraft/network/chat/Component;)V", at = @At("TAIL"))
    public void display(Component component, CallbackInfo ci) {
        if (!MinecraftExtKt.isOnline()) return;
        SidebarEvents.INSTANCE.getLINE_UPDATE().invoker().onSidebarLine(component);
    }
}

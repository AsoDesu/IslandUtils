package net.asodev.islandutils.mixins;

import net.asodev.islandutils.util.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Component.class)
public interface ComponentTranslateMixin {

    @Inject(method = "translatable(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;", at = @At("TAIL"), cancellable = true)
    private static void init(String string, CallbackInfoReturnable<MutableComponent> cir) {
        if (string.equalsIgnoreCase("soundCategory.record")) {
            if (Minecraft.getInstance().getCurrentServer() != null) {
                if (Minecraft.getInstance().getCurrentServer().ip.contains("mccisland")) {
                    cir.setReturnValue(Component.literal(ChatUtils.translate("&bMCC Music")));
                }
            }
        }
    }

}

package net.asodev.islandutils.mixins.reporting;

import net.asodev.islandutils.modules.reporting.ReportHandler;
import net.minecraft.client.gui.screens.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {

    @Inject(
            method = "handleChatInput",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;sendCommand(Ljava/lang/String;)V",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    public void handleChatInput(String string, boolean bl, CallbackInfoReturnable<Boolean> cir) {
        if (string.startsWith("/report") && ReportHandler.shouldChangeReporting()) {
            String[] args = string.substring(1).split(" ");
            if (args.length == 2) {
                ReportHandler.open(args[1]);
                cir.setReturnValue(true);
            }
        }
    }
}

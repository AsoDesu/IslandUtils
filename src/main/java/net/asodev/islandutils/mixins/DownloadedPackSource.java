package net.asodev.islandutils.mixins;

import net.asodev.islandutils.IslandUtilsEvents;
import net.minecraft.client.resources.server.PackLoadFeedback;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(net.minecraft.client.resources.server.DownloadedPackSource.class)
public class DownloadedPackSource {

    @Inject(method = "createPackResponseSender", at = @At("RETURN"), cancellable = true)
    private static void createPackResponseSender(Connection connection, CallbackInfoReturnable<PackLoadFeedback> cir) {
        var originalFeedback = cir.getReturnValue();
        var newFeedback = new PackLoadFeedback() {

            @Override
            public void reportUpdate(UUID uUID, Update update) {
                originalFeedback.reportUpdate(uUID, update);
            }

            @Override
            public void reportFinalResult(UUID uUID, FinalResult finalResult) {
                IslandUtilsEvents.PACK_FINAL_RESULT.invoker().packReportFinalResult(uUID, finalResult);
                originalFeedback.reportFinalResult(uUID, finalResult);
            }
        };
        cir.setReturnValue(newFeedback);
    }
}

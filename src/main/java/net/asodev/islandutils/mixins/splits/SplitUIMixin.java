package net.asodev.islandutils.mixins.splits;

import net.asodev.islandutils.modules.splits.LevelTimer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.LerpingBossEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;

@Mixin(BossHealthOverlay.class)
public class SplitUIMixin {

    @Shadow @Final private Map<UUID, LerpingBossEvent> events;

    @Inject(method = "render", at = @At("HEAD"))
    private void render(GuiGraphics guiGraphics, CallbackInfo ci) {
        LevelTimer instance = LevelTimer.getInstance();
        if (instance != null && instance.getUI() != null) instance.getUI().render(guiGraphics, this.events.values());
    }

}

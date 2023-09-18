package net.asodev.islandutils.mixins;

import net.asodev.islandutils.modules.plobby.PlobbyUI;
import net.asodev.islandutils.modules.splits.ui.SplitUI;
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

/**
 * We're using a mixin rather than HudRenderCallback because
 * it has some weird issues with transparency.
 * <p>
 * Also SplitUI needs BossBar events list
 */
@Mixin(BossHealthOverlay.class)
public class BossUIMixin {

    @Shadow @Final private Map<UUID, LerpingBossEvent> events;

    @Inject(method = "render", at = @At("HEAD"))
    private void render(GuiGraphics guiGraphics, CallbackInfo ci) {
        SplitUI.renderInstance(guiGraphics, this.events);
        PlobbyUI.renderInstance(guiGraphics);
    }

}

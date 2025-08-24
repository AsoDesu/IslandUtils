package net.asodev.islandutils.mixins;

import net.asodev.islandutils.modules.splits.ui.SplitUI;
import net.asodev.islandutils.state.MccIslandState;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import net.minecraft.client.gui.components.LerpingBossEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

/**
 * We're using a mixin rather than HudRenderCallback because
 * it has some weird issues with transparency.
 * <p>
 * Also SplitUI needs BossBar events list
 */
@Mixin(BossHealthOverlay.class)
public class BossUIMixin {

    @Shadow @Final
    Map<UUID, LerpingBossEvent> events;

    @Unique
    private static final List<String> QUEUE_BOSSBAR_PATTERNS = List.of("(((((", "TELEPORTING (", "TELEPORTED!");

    @Inject(method = "render", at = @At("HEAD"))
    private void render(GuiGraphics guiGraphics, CallbackInfo ci) {
        if (!MccIslandState.isOnline()) return;

        List<LerpingBossEvent> list = new ArrayList<>(events.values());
        Collections.reverse(list);
        int size = list.size();
        for (LerpingBossEvent event : list) {
            var eventName = event.getName().getString();
            if (eventName.isEmpty()) {
                size--;
            } else if (QUEUE_BOSSBAR_PATTERNS.stream().anyMatch(eventName::contains)) {
                size++;
            }
        }
        SplitUI.renderInstance(guiGraphics, size);
    }



}

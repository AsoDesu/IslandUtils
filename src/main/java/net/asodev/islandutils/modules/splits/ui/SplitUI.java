package net.asodev.islandutils.modules.splits.ui;

import net.asodev.islandutils.modules.splits.LevelTimer;
import net.asodev.islandutils.util.ChatUtils;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.GuiGraphics;

public interface SplitUI {
    void render(GuiGraphics guiGraphics, int bossBars);

    public static void renderInstance(GuiGraphics guiGraphics, int bossBars) {
        LevelTimer instance = LevelTimer.getInstance();
        if (instance != null && instance.getUI() != null) instance.getUI().render(guiGraphics, bossBars);
    }

    static void setupFallbackRenderer() {
        ChatUtils.debug("Setup fallback renderer for SplitUI");
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            renderInstance(drawContext, 1);
        });
    }
}

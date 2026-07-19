package net.asodev.islandutils.modules.splits.ui;

import net.asodev.islandutils.modules.splits.LevelTimer;
import net.asodev.islandutils.util.ChatUtils;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;

public interface SplitUI {
    void render(GuiGraphicsExtractor guiGraphics, int bossBars);

    public static void renderInstance(GuiGraphicsExtractor guiGraphics, int bossBars) {
        LevelTimer instance = LevelTimer.getInstance();
        if (instance != null && instance.getUI() != null) instance.getUI().render(guiGraphics, bossBars);
    }

    static void setupFallbackRenderer() {
        ChatUtils.debug("Setup fallback renderer for SplitUI");
        HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("islandutils", "split_ui"), (drawContext, tickDelta) -> {
            renderInstance(drawContext, 1);
        });
    }
}

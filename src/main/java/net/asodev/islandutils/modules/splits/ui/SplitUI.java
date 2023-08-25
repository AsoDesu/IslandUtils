package net.asodev.islandutils.modules.splits.ui;

import net.asodev.islandutils.modules.splits.LevelTimer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LerpingBossEvent;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public interface SplitUI {
    void render(GuiGraphics guiGraphics, Collection<LerpingBossEvent> events);

    public static void renderInstance(GuiGraphics guiGraphics, Map<UUID, LerpingBossEvent> events) {
        LevelTimer instance = LevelTimer.getInstance();
        if (instance != null && instance.getUI() != null) instance.getUI().render(guiGraphics, events.values());
    }
}

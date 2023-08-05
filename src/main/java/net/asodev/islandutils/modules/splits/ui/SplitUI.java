package net.asodev.islandutils.modules.splits.ui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LerpingBossEvent;

import java.util.Collection;

public interface SplitUI {
    void render(GuiGraphics guiGraphics, Collection<LerpingBossEvent> events);
}

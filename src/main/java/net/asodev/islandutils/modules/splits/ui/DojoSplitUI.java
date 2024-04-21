package net.asodev.islandutils.modules.splits.ui;

import net.asodev.islandutils.modules.splits.LevelTimer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class DojoSplitUI implements SplitUI {
    private static final ResourceLocation BAR_TEXTURE = new ResourceLocation("island", "textures/gui/pkw_splits.png");
    private static final int MCC_BAR_WIDTH = 130;
    public static Style MCC_HUD_STYLE = Style.EMPTY.withFont(new ResourceLocation("mcc", "hud"));

    private LevelTimer timer;

    public DojoSplitUI(LevelTimer timer) {
        this.timer = timer;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int bossBars) {
        int x = (guiGraphics.guiWidth() / 2) - (MCC_BAR_WIDTH / 2);
        int y = Double.valueOf((bossBars * 18.5)).intValue();
        guiGraphics.blit(BAR_TEXTURE, x, y, 0, 0, this.width(), this.height());

        renderLevelName(guiGraphics, x, y);
        renderSplitTime(guiGraphics, x, y);
        if (timer.options.isShowSplitImprovements()) {
            renderSplitImprovement(guiGraphics, x, y);
        }
    }
    public void renderLevelName(GuiGraphics guiGraphics, int x, int y) {
        Font font = Minecraft.getInstance().font; // Minecraft is incapable of getting this itself
        Component levelName = Component.literal(timer.getLevelName()).withStyle(MCC_HUD_STYLE);
        int LEVEL_NAME_WIDTH = 25; // Width of the dark area for the level name

        int txoff = (LEVEL_NAME_WIDTH / 2) - (font.width(levelName) / 2); // Offset needed to center the level name
        int tx = x + txoff + 1; // The X coordinate to render the level name
        int ty = y + 2; // The Y coordinate to render the level name
        guiGraphics.drawString(font, levelName, tx, ty, 16777215 | 255 << 24, true);
    }

    public void renderSplitTime(GuiGraphics guiGraphics, int x, int y) {
        Font font = Minecraft.getInstance().font;
        String formattedTime = String.format("%.3f", timer.getCurrentSplitTime());
        Component splitTime = Component.literal(formattedTime);
        int tx = x + this.width() - font.width(splitTime) - 2;
        int ty = y + 2;
        guiGraphics.drawString(font, splitTime, tx, ty, 16777215 | 255 << 24, true);
    }

    public void renderSplitImprovement(GuiGraphics guiGraphics, int x, int y) {
        if (timer.isBetween()) return;

        Double splitImprovement = timer.getSplitImprovement();
        if (splitImprovement == null) return;
        if (splitImprovement < timer.options.getShowTimerImprovementAt()) return;

        String formattedTime = String.format("%.2fs", splitImprovement);
        ChatFormatting color = ChatFormatting.GREEN;
        if (splitImprovement > 0) {
            color = ChatFormatting.RED;
            formattedTime = "+" + formattedTime;
        }

        Font font = Minecraft.getInstance().font;
        Component improvementTime = Component.literal(formattedTime).withStyle(MCC_HUD_STYLE.withColor(color));
        int tx = x + 12 + (this.width() / 2) - (font.width(improvementTime) / 2);
        int ty = y + 2;
        guiGraphics.drawString(font, improvementTime, tx, ty, 16777215 | 255 << 24, true);
    }


    private int width() {
        return 130;
    }
    private int height() {
        return 12;
    }
}

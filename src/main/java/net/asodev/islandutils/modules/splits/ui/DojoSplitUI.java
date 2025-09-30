package net.asodev.islandutils.modules.splits.ui;

import net.asodev.islandutils.modules.splits.LevelTimer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

public class DojoSplitUI implements SplitUI {
    private static final ResourceLocation BAR_TEXTURE = ResourceLocation.fromNamespaceAndPath("island", "pkw_splits");
    private static final int MCC_BAR_WIDTH = 130;
    public static Style MCC_HUD_STYLE = Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath("mcc", "hud"));
    public static Style SMALL_SPLIT_FONT_STYLE = Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath("island", "split"));

    private final LevelTimer timer;
    private int timerWidth;

    public DojoSplitUI(LevelTimer timer) {
        this.timer = timer;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int bossBars) {
        int x = Math.round(((float)guiGraphics.guiWidth() / 2.0f) - ((float)MCC_BAR_WIDTH / 2.0f) - 1.0f);
        int y = Double.valueOf((bossBars * 18.5)).intValue() + 1;
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, BAR_TEXTURE, x, y, this.width(), this.height());

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
        timerWidth = font.width(splitTime);
        int tx = x + this.width() - timerWidth - 2;
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
        Component improvementTime = Component.literal(formattedTime).withStyle(SMALL_SPLIT_FONT_STYLE.withColor(color));
        int tx = x + this.width() - timerWidth - 2 - font.width(improvementTime) - 8;
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

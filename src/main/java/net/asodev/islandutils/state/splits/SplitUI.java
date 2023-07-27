package net.asodev.islandutils.state.splits;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;

public class SplitUI {
    private static final ResourceLocation BAR_TEXTURE = new ResourceLocation("island", "textures/gui/pkw_splits.png");
    private static final int MCC_BAR_WIDTH = 130;
    private static Style MCC_HUD_STYLE = Style.EMPTY.withFont(new ResourceLocation("mcc", "hud"));


    public void render(GuiGraphics guiGraphics) {
        int x = (guiGraphics.guiWidth() / 2) - (MCC_BAR_WIDTH / 2);
        int y = 2 + 16;
        guiGraphics.blit(BAR_TEXTURE, x, y, 0, 0, this.width(), this.height());

        renderLevelName(guiGraphics, x, y);
        renderSplitTime(guiGraphics, x, y);
        renderSplitImprovement(guiGraphics, x, y);
    }
    public void renderLevelName(GuiGraphics guiGraphics, int x, int y) {
        Font font = Minecraft.getInstance().font; // Minecraft is incapable of getting this itself
        Component levelName = Component.literal("B2-1").withStyle(MCC_HUD_STYLE);
        int LEVEL_NAME_WIDTH = 25; // Width of the dark area for the level name

        int txoff = (LEVEL_NAME_WIDTH / 2) - (font.width(levelName) / 2); // Offset needed to center the level name
        int tx = x + txoff + 1; // The X coordinate to render the level name
        int ty = y + 2; // The Y coordinate to render the level name
        // draw :D
        guiGraphics.drawString(font, levelName, tx, ty, 16777215 | 255 << 24, true);
    }

    public void renderSplitTime(GuiGraphics guiGraphics, int x, int y) {
        Font font = Minecraft.getInstance().font;
        Component splitTime = Component.literal("32.100");
        int tx = x + this.width() - font.width(splitTime) - 2;
        int ty = y + 2;
        guiGraphics.drawString(font, splitTime, tx, ty, 16777215 | 255 << 24, true);
    }

    public void renderSplitImprovement(GuiGraphics guiGraphics, int x, int y) {
        Font font = Minecraft.getInstance().font; // Minecraft is still incapable of getting this itself
        Component improvementTime = Component.literal("-4.2s").withStyle(MCC_HUD_STYLE.withColor(ChatFormatting.GREEN));
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

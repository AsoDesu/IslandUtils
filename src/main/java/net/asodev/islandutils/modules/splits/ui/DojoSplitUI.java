package net.asodev.islandutils.modules.splits.ui;

import net.asodev.islandutils.modules.splits.LevelTimer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import static net.asodev.islandutils.util.FontUtils.*;

public class DojoSplitUI implements SplitUI {
    private static final ResourceLocation BAR_TEXTURE = ResourceLocation.fromNamespaceAndPath("island", "pkw_splits");
    private static final int MCC_BAR_WIDTH = 130;

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
        final int LEVEL_NAME_WIDTH = 25; // Width of the dark area for the level name

        Component levelName = Component.literal(timer.getLevelName()).withStyle(MCC_HUD_STYLE);
        int txoff = (LEVEL_NAME_WIDTH / 2) - (getMinecraftFont().width(levelName) / 2); // Offset needed to center the level name
        int tx = x + txoff + 1; // The X coordinate to render the level name
        int ty = y + 2; // The Y coordinate to render the level name
        guiGraphics.drawString(getMinecraftFont(), levelName, tx, ty, 16777215 | 255 << 24, true);
    }

    public void renderSplitTime(GuiGraphics guiGraphics, int x, int y) {
        String formattedTime = String.format("%.3f", timer.getCurrentSplitTime());
        Component splitTime = Component.literal(formattedTime);
        timerWidth = getMinecraftFont().width(splitTime);
        int tx = x + this.width() - timerWidth - 2;
        int ty = y + 2;
        guiGraphics.drawString(getMinecraftFont(), splitTime, tx, ty, 16777215 | 255 << 24, true);
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
        Component improvementTime = Component.literal(formattedTime).withStyle(CUSTOM_SPLIT_STYLE.withColor(color));
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

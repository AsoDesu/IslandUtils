package net.asodev.islandutils.modules.reporting.reportui;

import net.asodev.islandutils.modules.reporting.ReportHandler;
import net.asodev.islandutils.modules.reporting.playersui.ReportingPlayersScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;

public class ReportOptionsList extends ObjectSelectionList<ReportOptionEntry> {

    private final ReportOptionsScreen screen;

    public ReportOptionsList(ReportOptionsScreen screen) {
        super(Minecraft.getInstance(), screen.width, screen.height, 64, screen.height - 96, 18);
        this.screen = screen;

        setRenderHeader(false, 0);
        setRenderTopAndBottom(false);
        setRenderBackground(false);
        for (String reason : ReportHandler.getReasons()) {
            this.addEntry(new ReportOptionEntry(reason, this, screen));
        }
    }

    @Override
    protected void enableScissor(GuiGraphics graphics) {
        graphics.enableScissor(this.x0, this.y0 + 2, this.x1, this.y1);
    }

    @Override
    protected int getScrollbarPosition() {
        return this.width / 2 + 90;
    }

    @Override
    public int getRowWidth() {
        return 150;
    }

    @Override
    protected void renderBackground(GuiGraphics graphics) {
        graphics.fillGradient(0, 0, this.width, this.height, 0xc0101010, 0xd0101010);
        graphics.blitNineSliced(ReportingPlayersScreen.TEXTURE, getRowLeft() - 10, this.y0 - 6, getRowWidth() + 16, this.y1 - this.y0 + 14, 8, 236, 34, 1, 1);

        graphics.drawCenteredString(Minecraft.getInstance().font, "Reporting - " + this.screen.username(), this.width / 2, this.y0 - 6 - 14, 0XFFFFFF);
    }

    public int width() {
        return this.width;
    }

}

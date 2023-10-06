package net.asodev.islandutils.modules.reporting.reportui;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class ReportOptionEntry extends ObjectSelectionList.Entry<ReportOptionEntry> {

    private final String reason;
    private final Component component;

    private final ReportOptionsList list;
    private final ReportOptionsScreen screen;

    private long lastClickTime;

    public ReportOptionEntry(String reason, ReportOptionsList list, ReportOptionsScreen screen) {
        this.reason = reason;
		this.component = Component.translatableWithFallback("report.reason." + reason, reason);
        this.list = list;
        this.screen = screen;
    }

    @Override
    public void render(GuiGraphics graphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTicks) {
        graphics.drawCenteredString(Minecraft.getInstance().font, this.component, this.list.width() / 2, top + (height - 9) / 2, 0xFFFFFF);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == InputConstants.MOUSE_BUTTON_LEFT) {
            this.list.setSelected(this);
            if (Util.getMillis() - this.lastClickTime < 250L) {
                this.screen.complete();
            }

            this.lastClickTime = Util.getMillis();
            return true;
        } else {
            this.lastClickTime = Util.getMillis();
            return false;
        }
    }

    @Override
    public @NotNull Component getNarration() {
        return Component.translatable("narrator.select", this.component);
    }

    public String reason() {
        return this.reason;
    }
}
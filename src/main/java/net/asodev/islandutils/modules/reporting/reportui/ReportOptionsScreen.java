package net.asodev.islandutils.modules.reporting.reportui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.navigation.CommonInputs;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.CommonComponents;

public class ReportOptionsScreen extends Screen {

    private final String username;
    private ReportOptionsList list;

    public ReportOptionsScreen(String username) {
        super(CommonComponents.EMPTY);
        this.username = username;
    }

    @Override
    protected void init() {
        this.list = this.addWidget(new ReportOptionsList(this));
        this.addRenderableWidget(
            Button.builder(CommonComponents.GUI_CANCEL, (button) -> this.onClose())
                .bounds(this.width / 2 - 155, this.height - 38, 150, 20)
                .build()
        );
        this.addRenderableWidget(
            Button.builder(CommonComponents.GUI_DONE, (button) -> this.complete())
                .bounds(this.width / 2 - 155 + 160, this.height - 38, 150, 20)
                .build()
        );
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (CommonInputs.selected(keyCode)) {
            ReportOptionEntry entry = this.list.getSelected();
            if (entry != null) {
                this.list.setSelected(entry);
                this.complete();
                return true;
            }
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.list.render(graphics, mouseX, mouseY, partialTicks);
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public void complete() {
        ReportOptionEntry entry = this.list.getSelected();
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (entry != null && connection != null) {
            connection.sendCommand("report " + this.username + " " + entry.reason());
            this.onClose();
        }
    }

    public String username() {
        return this.username;
    }
}

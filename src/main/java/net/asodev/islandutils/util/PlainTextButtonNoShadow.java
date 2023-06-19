package net.asodev.islandutils.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;

public class PlainTextButtonNoShadow extends Button {
    private final Font font;
    private final Component message;
    private final Component underlinedMessage;

    public PlainTextButtonNoShadow(int i, int j, int k, int l, Component component, Button.OnPress onPress, Font font) {
        super(i, j, k, l, component, onPress, DEFAULT_NARRATION);
        this.font = font;
        this.message = component;
        this.underlinedMessage = ComponentUtils.mergeStyles(component.copy(), Style.EMPTY.withUnderlined(true));
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        Component component = this.isHoveredOrFocused() ? this.underlinedMessage : this.message;
        guiGraphics.drawString(this.font, component, getX(), getY(), 0xFFFFFF | Mth.ceil(this.alpha * 255.0f) << 24, false);
    }
}

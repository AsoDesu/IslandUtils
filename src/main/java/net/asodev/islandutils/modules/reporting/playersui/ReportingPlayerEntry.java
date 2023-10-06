package net.asodev.islandutils.modules.reporting.playersui;

import net.asodev.islandutils.modules.reporting.reportui.ReportOptionsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.social.PlayerEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ReportingPlayerEntry extends ContainerObjectSelectionList.Entry<ReportingPlayerEntry> {

	private static final ResourceLocation REPORT_BUTTON_LOCATION = new ResourceLocation("textures/gui/report_button.png");
	private static final Component REPORT_PLAYER_TOOLTIP = Component.translatable("gui.socialInteractions.tooltip.report");

	private final Minecraft minecraft;
	private final List<AbstractWidget> children;
	private final String playerName;
	@Nullable
	private final Supplier<ResourceLocation> skinGetter;
	private final Button reportButton;
	private float tooltipHoverTime;

	public ReportingPlayerEntry(Minecraft minecraft, String name, @Nullable Supplier<ResourceLocation> skinGetter) {
		this.minecraft = minecraft;
		this.playerName = name;
		this.skinGetter = skinGetter;
		this.reportButton = new ImageButton(
				0, 0, 20, 20,
				0, 0, 20,
				REPORT_BUTTON_LOCATION, 64, 64,
				button -> minecraft.setScreen(new ReportOptionsScreen(name)),
				Component.translatable("gui.socialInteractions.report")
		);
		this.reportButton.setTooltip(Tooltip.create(REPORT_PLAYER_TOOLTIP, Component.translatable("gui.socialInteractions.narration.report", this.playerName)));
		this.reportButton.setTooltipDelay(10);
		this.children = new ArrayList<>();
		this.children.add(this.reportButton);
	}

	@Override
	public void render(GuiGraphics graphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTicks) {
		graphics.fill(left, top, left + width, top + height, PlayerEntry.BG_FILL);

		if (this.skinGetter != null) {
			PlayerFaceRenderer.draw(graphics, this.skinGetter.get(), left + 4, top + (height - 24) / 2, 24);
		}
		graphics.drawString(this.minecraft.font, this.playerName, left + 4 + 24 + 4, top + (height - 9) / 2, PlayerEntry.PLAYERNAME_COLOR, false);

		float hoverTime = this.tooltipHoverTime;
		this.reportButton.setX(left + (width - this.reportButton.getWidth() - 4));
		this.reportButton.setY(top + (height - this.reportButton.getHeight()) / 2);
		this.reportButton.render(graphics, mouseX, mouseY, partialTicks);
		if (hoverTime == this.tooltipHoverTime) {
			this.tooltipHoverTime = 0.0F;
		}
	}

	@Override
	public @NotNull List<? extends GuiEventListener> children() {
		return this.children;
	}

	@Override
	public @NotNull List<? extends NarratableEntry> narratables() {
		return this.children;
	}

	public String getPlayerName() {
		return this.playerName;
	}
}

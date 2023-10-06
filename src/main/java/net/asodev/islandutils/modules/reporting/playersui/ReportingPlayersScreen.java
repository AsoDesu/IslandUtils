package net.asodev.islandutils.modules.reporting.playersui;

import net.asodev.islandutils.modules.reporting.ReportHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;

public class ReportingPlayersScreen extends Screen {

	public static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/social_interactions.png");

	private static final Component SEARCH_HINT = Component.translatable("gui.socialInteractions.search_hint").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY);
	private static final Component EMPTY_SEARCH = Component.translatable("gui.socialInteractions.search_empty").withStyle(ChatFormatting.GRAY);

	private ReportingPlayersList reportingList = null;
	private EditBox searchBox;
	private String lastSearch = "";
	private boolean initialized;

	public ReportingPlayersScreen() {
		super(CommonComponents.EMPTY);

		ReportHandler.load(() -> {
			if (this.reportingList != null) {
				this.reportingList.updatePlayerList(ReportHandler.getPlayers());
			}
		});
	}

	private int windowHeight() {
		return Math.max(52, this.height - 128 - 16);
	}

	private int listEnd() {
		return 80 + this.windowHeight() - 8;
	}

	private int marginX() {
		return (this.width - 238) / 2;
	}

	@Override
	public void tick() {
		super.tick();
		this.searchBox.tick();
	}

	@Override
	protected void init() {
		if (this.initialized) {
			this.reportingList.updateSize(this.width, this.height, 88, this.listEnd());
		} else {
			this.reportingList = new ReportingPlayersList(this.minecraft, this.width, this.height, 88, this.listEnd(), 36);
		}

		String string = this.searchBox != null ? this.searchBox.getValue() : "";
		this.searchBox = new EditBox(this.font, this.marginX() + 29, 75, 198, 13, SEARCH_HINT);
		this.searchBox.setMaxLength(16);
		this.searchBox.setVisible(true);
		this.searchBox.setTextColor(0xFFFFFF);
		this.searchBox.setValue(string);
		this.searchBox.setHint(SEARCH_HINT);
		this.searchBox.setResponder(this::checkSearchStringUpdate);
		this.addWidget(this.searchBox);
		this.addWidget(this.reportingList);

		this.initialized = true;
		this.reportingList.updatePlayerList(ReportHandler.getPlayers());
	}

	@Override
	public void renderBackground(GuiGraphics graphics) {
		super.renderBackground(graphics);
		graphics.blitNineSliced(TEXTURE, this.marginX() + 3, 64, 236, this.windowHeight() + 16, 8, 236, 34, 1, 1);
		graphics.blit(TEXTURE, this.marginX() + 13, 76, 243, 1, 12, 12);
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(graphics);

		graphics.drawString(Minecraft.getInstance().font, "MCC Island - " + this.reportingList.children().size() + " players", this.marginX() + 8, 52, -1);

		if (!this.reportingList.isEmpty()) {
			this.reportingList.render(graphics, mouseX, mouseY, partialTicks);
		} else if (!this.searchBox.getValue().isEmpty()) {
			graphics.drawCenteredString(Minecraft.getInstance().font, EMPTY_SEARCH, this.width / 2, (72 + this.listEnd()) / 2, -1);
		}

		this.searchBox.render(graphics, mouseX, mouseY, partialTicks);
		super.render(graphics, mouseX, mouseY, partialTicks);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (!this.searchBox.isFocused() && Minecraft.getInstance().options.keySocialInteractions.matches(keyCode, modifiers)) {
			onClose();
			return true;
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private void checkSearchStringUpdate(String string) {
		string = string.toLowerCase(Locale.ROOT);
		if (!string.equals(this.lastSearch)) {
			this.reportingList.setFilter(string);
			this.lastSearch = string;
			this.reportingList.updatePlayerList(ReportHandler.getPlayers());
		}
	}
}
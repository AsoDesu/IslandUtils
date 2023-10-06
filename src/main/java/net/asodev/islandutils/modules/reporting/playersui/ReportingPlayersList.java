package net.asodev.islandutils.modules.reporting.playersui;

import com.google.common.base.Suppliers;
import net.minecraft.Optionull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ReportingPlayersList extends ContainerObjectSelectionList<ReportingPlayerEntry> {

	private final List<ReportingPlayerEntry> players = new ArrayList<>();
	@Nullable
	private String filter;

	public ReportingPlayersList(Minecraft minecraft, int width, int height, int y0, int y1, int itemHeight) {
		super(minecraft, width, height, y0, y1, itemHeight);
		this.setRenderBackground(false);
		this.setRenderTopAndBottom(false);
	}

	@Override
	protected void enableScissor(GuiGraphics graphics) {
		graphics.enableScissor(this.x0, this.y0 + 4, this.x1, this.y1);
	}

	public void updatePlayerList(Collection<String> collection) {
		this.addOnlinePlayers(collection);
		this.updateFiltersAndScroll(this.getScrollAmount());
	}

	private void addOnlinePlayers(Collection<String> players) {
		this.players.clear();
		String self = this.minecraft.getUser().getName();
		ClientPacketListener connection = this.minecraft.getConnection();
		if (connection == null) return;

		for (String player : players) {
			if (player.equalsIgnoreCase(self)) {
				continue;
			}
			PlayerInfo info = connection.getPlayerInfo(player);
			this.players.add(
					new ReportingPlayerEntry(
							this.minecraft, player,
							Optionull.mapOrDefault(info,
									i -> i::getSkinLocation,
									Suppliers.memoize(() -> DefaultPlayerSkin.getDefaultSkin(UUID.randomUUID()))
							)
					)
			);
		}
	}

	private void updateFiltersAndScroll(double d) {
		this.players.sort(Comparator.comparing(ReportingPlayerEntry::getPlayerName, String::compareToIgnoreCase));
		this.updateFilteredPlayers();
		this.replaceEntries(this.players);
		this.setScrollAmount(d);
	}

	private void updateFilteredPlayers() {
		if (this.filter != null) {
			this.players.removeIf(entry -> !entry.getPlayerName().toLowerCase(Locale.ROOT).contains(this.filter));
			this.replaceEntries(this.players);
		}

	}

	public void setFilter(@Nullable String string) {
		this.filter = string;
	}

	public boolean isEmpty() {
		return this.players.isEmpty();
	}
}
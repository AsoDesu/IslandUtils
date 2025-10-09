package net.asodev.islandutils.modules.scavenging;

import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.util.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Collection;
import java.util.List;

public class Scavenging {
    // the total width of the island menu
    private static final int GUI_TOTAL_WIDTH = 176;
    // the width of the menu body
    private static final int GUI_BODY_WIDTH = 164;
    // the offset between the end of the scavenging icons and the right-side of the body
    private static final int SCAVENGING_ICON_INNER_OFFSET = 24;

    private static Component titleComponent;
    private static ScavengingItemHandler dustHandler;
    private static ScavengingItemHandler silverHandler;
    private static ScavengingItemHandler coinHandler;

    public static boolean isScavengingMenuOrDisabled(AbstractContainerScreen<?> screen) {
        if (!IslandOptions.getMisc().isSilverPreview()) return false;
        if (titleComponent == null) return false;
        return screen.getTitle().contains(titleComponent);
    }
    public static void renderSilverTotal(ScavengingTotalList silverTotal, GuiGraphics guiGraphics) {
        Minecraft minecraft = Minecraft.getInstance();
        if (!(minecraft.screen instanceof ContainerScreen screen)) return;

        int bgX = (screen.width - GUI_TOTAL_WIDTH) / 2;
        int x = bgX + (GUI_TOTAL_WIDTH - SCAVENGING_ICON_INNER_OFFSET);

        Collection<ScavengingTotal> totals = silverTotal.totals.values();
        for (ScavengingTotal total : totals) {
            if (total.amount() <= 0L) continue;

            x -= total.handler().renderTotal(guiGraphics, total.amount(), x);
            x -= 5; // gap
        }
    }

    public static ScavengingTotalList getSilverTotal(ChestMenu menu) {
        ScavengingTotalList list = new ScavengingTotalList();

        Container container = menu.getContainer();
        applyItemRow(list, container, 11, 15);
        applyItemRow(list, container, 20, 25);
        return list;
    }
    private static void applyItemRow(ScavengingTotalList list, Container container, int min, int max) {
        for (int i = min; i <= max; i++) {
            ItemStack item = container.getItem(i);
            if (item.is(Items.AIR)) continue;
            applyItems(item, list);
        }
    }

    public static void applyItems(ItemStack item, ScavengingTotalList list) {
        List<Component> lores = Utils.getTooltipLines(item);
        if (lores == null) return;

        for (Component line : lores) {
            if (silverHandler != null) list.apply(silverHandler.checkLine(line));
            if (dustHandler != null) list.apply(dustHandler.checkLine(line));
            if (coinHandler != null) list.apply(coinHandler.checkLine(line));
        }
    }

    public static void setDustCharacter(String dustCharacter) {
        dustHandler = new ScavengingItemHandler("dust", dustCharacter);
    }
    public static void setSilverCharacter(String silverCharacter) {
        silverHandler = new ScavengingItemHandler("silver", silverCharacter);
    }
    public static void setCoinCharacter(String coinCharacter) {
        coinHandler = new ScavengingItemHandler("coin", coinCharacter);
    }

    public static void setTitleCharacter(String titleCharacter) {
        Scavenging.titleComponent = Component.literal(titleCharacter).withStyle(
                Style.EMPTY.withColor(ChatFormatting.WHITE).withFont(ResourceLocation.fromNamespaceAndPath("mcc", "chest_backgrounds"))
        );
    }
}

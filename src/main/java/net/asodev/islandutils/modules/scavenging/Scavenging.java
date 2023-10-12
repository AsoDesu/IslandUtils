package net.asodev.islandutils.modules.scavenging;

import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Collection;
import java.util.List;

import static net.asodev.islandutils.modules.cosmetics.CosmeticState.MCC_ICONS;

public class Scavenging {
    private static String titleCharacter;
    private static ScavengingItemHandler dustHandler;
    private static ScavengingItemHandler ticketHandler;

    public static boolean isScavengingMenuOrDisabled(AbstractContainerScreen<?> screen) {
        if (!IslandOptions.getMisc().isSilverPreview()) return false;
        return screen.getTitle().getString().contains(titleCharacter);
    }
    public static void renderSilverTotal(ScavengingTotalList silverTotal, GuiGraphics guiGraphics) {
        Minecraft minecraft = Minecraft.getInstance();
        if (!(minecraft.screen instanceof ContainerScreen screen)) return;

        int bgX = (screen.width - 176) / 2;
        int x = bgX + 160;

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
        for (int i = 37; i <= 43; i++) {
            ItemStack item = container.getItem(i);
            if (item.is(Items.AIR)) continue;
            applyItems(item, list);
        }
        return list;
    }
    public static void applyItems(ItemStack item, ScavengingTotalList list) {
        List<Component> lores = Utils.getLores(item);
        if (lores == null) return;
        int multiplier = item.getCount();

        for (Component line : lores) {
            list.apply(ticketHandler.checkLine(line));
            list.apply(dustHandler.checkLine(line));
        }
    }

    public static void setDustCharacter(String dustCharacter) {
        dustHandler = new ScavengingItemHandler("dust", dustCharacter);
    }
    public static void setTicketCharacter(String ticketCharacter) {
        ticketHandler = new ScavengingItemHandler("ticket", ticketCharacter, "\\[Knick Knax Ticket\\]");
    }

    public static void setTitleCharacter(String titleCharacter) {
        Scavenging.titleCharacter = titleCharacter;
    }
}

package net.asodev.islandutils.modules;

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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.asodev.islandutils.modules.cosmetics.CosmeticState.MCC_ICONS;

public class Scavenging {
    private static String titleCharacter;
    private static String silverCharacter;
    private static Pattern silverPattern;

    public static boolean isScavengingMenuOrDisabled(AbstractContainerScreen<?> screen) {
        if (!IslandOptions.getMisc().isSilverPreview()) return false;
        return screen.getTitle().getString().contains(titleCharacter);
    }
    public static void renderSilverTotal(long silverTotal, GuiGraphics guiGraphics) {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        if (!(minecraft.screen instanceof ContainerScreen screen)) return;

        Component silverComponent = Component.literal(String.valueOf(silverTotal))
                .append(Component.literal(silverCharacter).withStyle(Style.EMPTY.withFont(MCC_ICONS)));

        int bgX = (screen.width - 176) / 2;
        int bgY = (screen.height - 166) / 2;
        int x = bgX + 160 - font.width(silverComponent);
        int y = bgY + 89 + 4;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, 0, 105); // z-index: 105
        guiGraphics.drawString(font, silverComponent, x, y, 16777215, false);
        guiGraphics.pose().popPose();
    }
    public static long getSilverTotal(ChestMenu menu) {
        Container container = menu.getContainer();
        long silver = 0;
        for (int i = 37; i <= 43; i++) {
            ItemStack item = container.getItem(i);
            if (item.is(Items.AIR)) continue;
            silver += getSilver(item);
        }
        return silver;
    }
    public static long getSilver(ItemStack item) {
        List<Component> lores = Utils.getLores(item);
        if (lores == null) return 0L;
        for (Component line : lores) {
            String content = line.getString();
            Matcher matcher = silverPattern.matcher(content);
            if (!matcher.find()) continue;

            String silver = matcher.group(1);
            try { return Long.parseLong(silver); }
            catch (Exception ignored) {}
        }
        return 0L;
    }

    public static void setSilverCharacter(String silverCharacter) {
        Scavenging.silverCharacter = silverCharacter;
        Scavenging.silverPattern = Pattern.compile("(\\d+)" + silverCharacter);
    }

    public static void setTitleCharacter(String titleCharacter) {
        Scavenging.titleCharacter = titleCharacter;
    }
}

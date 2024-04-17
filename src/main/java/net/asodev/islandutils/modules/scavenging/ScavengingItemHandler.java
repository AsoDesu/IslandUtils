package net.asodev.islandutils.modules.scavenging;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.asodev.islandutils.modules.cosmetics.CosmeticState.MCC_ICONS;
import static net.asodev.islandutils.util.ChatUtils.iconsFontStyle;

public class ScavengingItemHandler {
    private static final int MENU_WIDTH = 176;
    private static final int MENU_HEIGHT = 166;

    private final String character;
    private final Pattern pattern;
    private final String name;
    private final ScavengingTotal total;

    public ScavengingItemHandler(String item, String character) {
        this(item, character, character);
    }
    public ScavengingItemHandler(String item, String character, String detectString) {
        this.character = character;
        this.pattern = Pattern.compile("(\\d+).*" + detectString);
        this.total = new ScavengingTotal(item, 0L, this);
        this.name = item;
    }

    public int renderTotal(GuiGraphics guiGraphics, Long total, int x) {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        if (!(minecraft.screen instanceof ContainerScreen screen)) return 0;

        Component silverComponent = Component.literal(String.valueOf(total))
                .append(Component.literal("_").withStyle(iconsFontStyle))
                .append(Component.literal(character).withStyle(Style.EMPTY.withFont(MCC_ICONS)));
        int width = font.width(silverComponent);

        x -= width;
        int bgY = (screen.height - MENU_HEIGHT) / 2;
        int y = bgY + 89 - 4;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, 0, 105); // z-index: 105
        guiGraphics.drawString(font, silverComponent, x, y, 16777215, false);
        guiGraphics.pose().popPose();

        return width;
    }

    public ScavengingTotal checkLine(Component line) {
        String content = line.getString().replace(",", "");
        Matcher matcher = pattern.matcher(content);
        if (!matcher.find()) return total;

        String amountText = matcher.group(1);
        try {
            long amount = Long.parseLong(amountText);
            return total.create(amount);
        } catch (Exception ignored) {}
        return total;
    }

    public String getCharacter() {
        return character;
    }
    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScavengingItemHandler that = (ScavengingItemHandler) o;
        return Objects.equals(name, that.name);
    }
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

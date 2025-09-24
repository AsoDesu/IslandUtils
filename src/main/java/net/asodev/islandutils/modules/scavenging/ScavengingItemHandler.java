package net.asodev.islandutils.modules.scavenging;

import net.asodev.islandutils.util.FontUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScavengingItemHandler {
    private static final int MENU_WIDTH = 176;
    private static final int CHEST_MENU_HEIGHT = 222;

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
                .append(Component.literal("_").withStyle(FontUtils.MCC_ICONS_STYLE))
                .append(Component.literal(character).withStyle(FontUtils.MCC_ICONS_STYLE));
        int width = font.width(silverComponent);

        x -= width;

        // 25 = y offset between label y & top of menu
        int topPos = ((screen.height - CHEST_MENU_HEIGHT) / 2) - 25;
        // 152 = y pos of top of footer
        // 20 = y pos from top of footer to top of button
        // 5 = bottom padding
        int y = topPos + 152 + 20 - 5;
        guiGraphics.drawString(font, silverComponent, x, y, 16777215, false);

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
        } catch (Exception ignored) {
        }
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

package net.asodev.islandutils.modules.plobby;

import com.mojang.blaze3d.systems.RenderSystem;
import net.asodev.islandutils.modules.plobby.state.PlobbyStateProvider;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.options.categories.PlobbyOptions;
import net.asodev.islandutils.state.Game;
import net.asodev.islandutils.state.MccIslandState;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;

import static net.asodev.islandutils.util.ChatUtils.iconsFontStyle;
import static net.asodev.islandutils.util.Utils.MCC_HUD_FONT;

public class PlobbyUI {
    private static Component LOCKED = Component.literal("\ue010").withStyle(iconsFontStyle.withColor(ChatFormatting.WHITE));
    private static Component UNLOCKED = Component.literal("\ue009").withStyle(iconsFontStyle.withColor(ChatFormatting.WHITE));
    private static Style NICE_YELLOW = Style.EMPTY.withColor(TextColor.parseColor("#ffff00"));

    PlobbyStateProvider stateProvider;
    public PlobbyUI(PlobbyStateProvider stateProvider) {
        this.stateProvider = stateProvider;
    }

    public void render(GuiGraphics guiGraphics) {
        PlobbyOptions options = IslandOptions.getPlobby();
        if (!options.showOnScreen()) return;
        if (!options.showInGame() && MccIslandState.getGame() != Game.HUB) return;
        if (!stateProvider.hasJoinCode()) return;

        Style lockedComponentStyle = Style.EMPTY.withFont(Style.DEFAULT_FONT).withColor(ChatFormatting.WHITE).withBold(true);
        Component component = stateProvider.isLocked()
                ? LOCKED.copy().append(Component.literal(" Plobby Locked!").withStyle(lockedComponentStyle))
                : UNLOCKED.copy().append(Component.literal(" Plobby Open!").withStyle(lockedComponentStyle));

        int width = 130;
        int screenWidth = guiGraphics.guiWidth();
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(screenWidth - width - 8, 6, 0f);

        int x = 0;
        int y = 0;
        int backgroundColor = Minecraft.getInstance().options.getBackgroundColor(Integer.MIN_VALUE);

        guiGraphics.fill(x, y, x + 130, y + 12, backgroundColor);
        guiGraphics.drawString(Minecraft.getInstance().font, component, x + 3, x + 2, 16777216);

        if (!stateProvider.isLocked()) {
            Component joinWith = Component.literal("JOIN CODE: ").withStyle(MCC_HUD_FONT.withColor(ChatFormatting.WHITE))
                    .append(Component.literal(stateProvider.getJoinCode()).withStyle(NICE_YELLOW));
            width -= 120;
            y = 13;
            x += width;
            guiGraphics.fill(x, y, x + 120, y + 10, backgroundColor);
            guiGraphics.drawString(Minecraft.getInstance().font, joinWith, x+ 3, y + 1, 16777216);
        }

        guiGraphics.pose().popPose();
    }

    public static void renderInstance(GuiGraphics guiGraphics) {
        Plobby instance = Plobby.getInstance();
        if (instance != null) instance.getUi().render(guiGraphics);
    }

}

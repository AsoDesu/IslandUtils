package net.asodev.islandutils.util;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.asodev.islandutils.IslandUtilsEvents;
import net.asodev.islandutils.modules.splits.LevelTimer;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.options.categories.MiscOptions;
import net.asodev.islandutils.state.Game;
import net.asodev.islandutils.state.MccIslandState;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class IngameIcons {

    private static final Minecraft MC = Minecraft.getInstance();

    private static boolean draw = false;

    public static void init() {
        HudRenderCallback.EVENT.register((GuiGraphics guiGraphics, DeltaTracker deltaTracker) -> {
            if (IslandOptions.getMisc().useGameIcons())
                drawCenteredIcon(guiGraphics, MccIslandState.getGame().getIslandId(), 128);
        });

        IslandUtilsEvents.GAME_CHANGE.register((game) -> {
            if (game == Game.HUB || !IslandOptions.getMisc().useGameIcons())
                return;

            new Thread(() -> {
                try {
                    draw = true;
                    Thread.sleep(5100);
                    draw = false;
                    MC.options.hideGui = false;
                } catch (Exception ignored) {

                }
            }).start();
        });
    }

    public static void drawCenteredIcon(GuiGraphics guiGraphics, String name, int size) {
        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath("island", "textures/gui/games/" + name + ".png");

        if (MC.screen != null || !isTexture(texture))
            return;

        if (draw) {
            guiGraphics.blit(texture, (int) (MC.getWindow().getWidth() / MC.getWindow().getGuiScale() / 2 - size / 2), (int) (MC.getWindow().getHeight() / MC.getWindow().getGuiScale() / 2 - size / 2), 0, 0, size, size, size, size);
            MC.options.hideGui = true;
        }
    }

    private static boolean isTexture(ResourceLocation texture) {
        try {
            return MC.getResourceManager().getResource(texture).isPresent();
        } catch (Exception e) {
            return false;
        }
    }
}

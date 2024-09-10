package net.asodev.islandutils.util;

import net.asodev.islandutils.state.MccIslandState;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class IngameIcons {

    private static final Minecraft MC = Minecraft.getInstance();

    public static void init() {
        HudRenderCallback.EVENT.register((GuiGraphics guiGraphics, DeltaTracker deltaTracker) -> drawCenteredIcon(guiGraphics, MccIslandState.getGame().getIslandId(), 128));
    }

    public static void drawCenteredIcon(GuiGraphics guiGraphics, String name, int size) {
        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath("island", "textures/gui/games/" + name + ".png");

        if (MC.screen != null || MC.options.hideGui || !isTexture(texture) || !isTitlePresent())
            return;

        guiGraphics.blit(texture, (int) (MC.getWindow().getWidth() / MC.getWindow().getGuiScale() / 2 - size / 2), (int) (MC.getWindow().getHeight() / MC.getWindow().getGuiScale() / 2 - size / 2), 0, 0, size, size, size, size);
    }

    private static boolean isTexture(ResourceLocation texture) {
        try {
            return MC.getResourceManager().getResource(texture).isPresent();
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isTitlePresent() {
        return true; // ToDo: Add Check
    }
}

package net.asodev.islandutils.modules;

import net.asodev.islandutils.util.Utils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;

import java.util.List;

public class FishingUpgradeIcon {
    private static final ResourceLocation UPGRADE_ICON_LOCATION = ResourceLocation.fromNamespaceAndPath("island", "upgrade");

    public static void render(Slot slot, GuiGraphics guiGraphics) {
        List<Component> lore = Utils.getTooltipLines(slot.getItem());
        if (lore == null) return;
        boolean canUpgrade = lore.stream().anyMatch(c -> c.getString().contains("Left-Click to Upgrade"));
        if (!canUpgrade) return;

        int x = slot.x + 1;
        int y = slot.y + 1;
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, UPGRADE_ICON_LOCATION, x, y, 16, 16);
    }

}

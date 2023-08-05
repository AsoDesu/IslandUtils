package net.asodev.islandutils.mixins.ui;

import de.jcm.discordgamesdk.activity.Activity;
import net.asodev.islandutils.discord.DiscordPresenceUpdator;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.state.MccIslandState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Gui.class)
public class ActivityDebugMixin {
    @Inject(method = "render", at = @At("TAIL"))
    private void render(GuiGraphics guiGraphics, float f, CallbackInfo ci) {
        Activity activity = DiscordPresenceUpdator.activity;
        if (activity == null
                || !MccIslandState.isOnline()
                || !IslandOptions.getMisc().isDebugActivityUi()) return;

        Font font = Minecraft.getInstance().font;
        List<String> activityPreview = List.of(
            activity.getDetails(),
            activity.getState(),
            String.format("%s [%s]", activity.assets().getSmallText(), activity.assets().getSmallImage()),
            String.format("%s [%s]", activity.assets().getLargeText(), activity.assets().getLargeImage())
        );

        guiGraphics.drawString(font, Component.literal("Activity Debug:"), 5, 30, 16777215 | 255 << 24, true);
        for (String text : activityPreview) {
            guiGraphics.drawString(font, Component.literal(text), 10, 42 + (9 * activityPreview.indexOf(text)), 16777215 | 255 << 24, true);
        }
    }
}

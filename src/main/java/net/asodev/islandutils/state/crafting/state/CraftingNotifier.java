package net.asodev.islandutils.state.crafting.state;

import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.state.crafting.CraftingMenuType;
import net.asodev.islandutils.state.crafting.CraftingToast;
import net.asodev.islandutils.util.ChatUtils;
import net.asodev.islandutils.util.MusicUtil;
import net.asodev.islandutils.util.Scheduler;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

import java.util.Date;

import static net.asodev.islandutils.util.ChatUtils.iconsFontStyle;

public class CraftingNotifier implements ClientTickEvents.EndTick {
    private int tick = 0;
    public CraftingNotifier() {
        ClientTickEvents.END_CLIENT_TICK.register(this);
    }

    public void update(Minecraft client) {
        boolean anythingHasChanged = false;
        for (CraftingItem item : CraftingItems.getItems()) {
            if (!item.isComplete()) continue;
            if (!MccIslandState.isOnline() || client.getOverlay() instanceof LoadingOverlay) continue;
            if (item.hasSentNotif()) continue;

            sendNotif(client, item);
            anythingHasChanged = true;
        }
        if (anythingHasChanged) CraftingItems.save();
    }

    public void sendNotif(Minecraft client, CraftingItem item) {
        IslandOptions options = IslandOptions.getOptions();
        item.setHasSentNotif(true);

        if (!options.isEnableCraftingNotifs()) return;
        boolean shouldMakeSound = false;
        if (options.isToastNotif()) {
            client.getToasts().addToast( new CraftingToast(item) );
            shouldMakeSound = true;
        }
        if (options.isChatNotif()) {
            sendChatNotif(item);
            shouldMakeSound = true;
        }

        if (shouldMakeSound) {
            sendNotifSound();
        }
    }

    private void sendChatNotif(CraftingItem item) {
        String icon = item.getCraftingMenuType() == CraftingMenuType.FORGE ? "\ue006" : "\ue007";
        Component iconComponent = Component.literal(icon).withStyle(iconsFontStyle);

        Style darkGreenColor = Style.EMPTY.withColor(ChatFormatting.DARK_GREEN);
        Component component = Component.literal("(").withStyle(darkGreenColor)
                .append(iconComponent)
                .append(Component.literal(") ").withStyle(darkGreenColor))
                .append(item.getTitle())
                .append(Component.literal(" has finished crafting!").withStyle(darkGreenColor));
        ChatUtils.send(component);
    }

    private void sendNotifSound() {
        SimpleSoundInstance mcc = MusicUtil.createSoundInstance(new ResourceLocation("mcc", "ui.achievement_receive"));
        Scheduler.schedule(5, (mc) -> {
            mc.getSoundManager().play(mcc);
        });
    }

    @Override
    public void onEndTick(Minecraft client) {
        tick++;
        if (tick >= 20) {
            update(client);
            tick = 0;
        }
    }
}

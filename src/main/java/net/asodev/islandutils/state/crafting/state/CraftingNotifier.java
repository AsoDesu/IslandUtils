package net.asodev.islandutils.state.crafting.state;

import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.state.crafting.CraftingToast;
import net.asodev.islandutils.util.MusicUtil;
import net.asodev.islandutils.util.Scheduler;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Date;

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
        Toast toast = new CraftingToast(item);
        client.getToasts().addToast(toast);
        SimpleSoundInstance mcc = MusicUtil.createSoundInstance(new ResourceLocation("mcc", "ui.achievement_receive"));
        Scheduler.schedule(5, (mc) -> {
            mc.getSoundManager().play(mcc);
        });

        item.setHasSentNotif(true);
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

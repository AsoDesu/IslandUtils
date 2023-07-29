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
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.util.Date;
import java.util.List;

import static net.asodev.islandutils.util.ChatUtils.iconsFontStyle;
import static net.asodev.islandutils.util.Utils.MCC_HUD_FONT;

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
        Style darkGreenColor = Style.EMPTY.withColor(ChatFormatting.DARK_GREEN);
        Component component = Component.literal("(").withStyle(darkGreenColor)
                .append(item.getTypeIcon())
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

    public static Component activeCraftsMessage() {
        Component newLine = Component.literal("\n").withStyle(Style.EMPTY);
        MutableComponent component = Component.literal("\nCRAFTING ITEMS:").withStyle(MCC_HUD_FONT);
        component.append(newLine);

        int i = 0;
        List<CraftingItem> itemList = CraftingItems.getItems();
        for (CraftingItem item : itemList) {
            i++;

            long timeRemaining = item.getFinishesCrafting() - System.currentTimeMillis();
            String timeText;
            if (timeRemaining > 0) {
                timeText = DurationFormatUtils.formatDuration(timeRemaining, "H'h' m'm' s's'");
            } else {
                timeText = "Complete";
            }

            Component itemComponent = Component.literal(" ").withStyle(Style.EMPTY.withFont(Style.DEFAULT_FONT))
                    .append(item.getTypeIcon())
                    .append(" ")
                    .append(item.getTitle())
                    .append(Component.literal(" - ").withStyle(ChatFormatting.DARK_GRAY))
                    .append(Component.literal(timeText).withStyle(ChatFormatting.RED));

            component.append(itemComponent);
            if (i < itemList.size()) component.append(newLine);
        }
        return component;
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

package net.asodev.islandutils.modules.crafting.state;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.asodev.islandutils.modules.crafting.CraftingToast;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.options.categories.CraftingOptions;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.util.ChatUtils;
import net.asodev.islandutils.util.IslandSoundEvents;
import net.asodev.islandutils.util.Scheduler;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.util.List;

import static net.asodev.islandutils.util.IslandUtilsCommand.cantUseDebugError;
import static net.asodev.islandutils.util.Utils.MCC_HUD_FONT;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class CraftingNotifier implements ClientTickEvents.EndTick {
    private int tick = 0;

    public void register() {
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
        CraftingOptions options = IslandOptions.getCrafting();
        item.setHasSentNotif(true);

        if (!options.isEnableCraftingNotifs()) return;
        boolean shouldMakeSound = false;
        if (options.isToastNotif()) {
            client.getToastManager().addToast(new CraftingToast(item));
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
                .append(" ")
                .append(Component.translatable("islandutils.message.crafting.chatNotif").withStyle(darkGreenColor));
        ChatUtils.send(component);
    }

    private void sendNotifSound() {
        SimpleSoundInstance mcc = SimpleSoundInstance.forUI(IslandSoundEvents.UI_ACHIEVEMENT_RECEIVE, 1f, 1f);
        Scheduler.schedule(5, (mc) -> {
            mc.getSoundManager().play(mcc);
        });
    }

    public static Component activeCraftsMessage() {
        Component newLine = Component.literal("\n").withStyle(Style.EMPTY);
        MutableComponent component = ChatUtils.checkForHudUnsupportedSymbols(I18n.get("islandutils.message.crafting.activeCraftsTitle")) ? 
                Component.translatable("islandutils.message.crafting.activeCraftsTitle").withStyle(MCC_HUD_FONT) :
                Component.translatable("islandutils.message.crafting.activeCraftsTitle");
        component.append(newLine);

        int i = 0;
        List<CraftingItem> itemList = CraftingItems.getItems();
        for (CraftingItem item : itemList) {
            i++;

            long timeRemaining = item.getFinishesCrafting() - System.currentTimeMillis();
            String timeText;
            ChatFormatting timeColor;
            if (timeRemaining > 0) {
                timeText = DurationFormatUtils.formatDuration(timeRemaining, "H'h' m'm' s's'");
                timeColor = ChatFormatting.RED;
            } else {
                timeText = I18n.get("islandutils.message.crafting.complete");
                timeColor = ChatFormatting.DARK_GREEN;
            }

            Component itemComponent = Component.literal(" ").withStyle(Style.EMPTY.withFont(Style.DEFAULT_FONT))
                    .append(item.getTypeIcon())
                    .append(" ")
                    .append(item.getTitle())
                    .append(Component.literal(" - ").withStyle(ChatFormatting.DARK_GRAY))
                    .append(Component.literal(timeText).withStyle(timeColor));

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

    public static LiteralArgumentBuilder<FabricClientCommandSource> getDebugCommand() {
        return literal("add_craft")
                .then(argument("color", StringArgumentType.string())
                .then(argument("slot", IntegerArgumentType.integer())
                .then(argument("delay", IntegerArgumentType.integer())
                .executes(ctx -> {
                    if (!IslandOptions.getMisc().isDebugMode()) {
                        ctx.getSource().sendError(cantUseDebugError);
                        return 0;
                    }
                    String color = ctx.getArgument("color", String.class);
                    Integer slot = ctx.getArgument("slot", Integer.class);
                    Integer delay = ctx.getArgument("delay", Integer.class);
                    CraftingItems.addDebugItem(color, slot, delay);
                    return 1;
                }))));
    }
}

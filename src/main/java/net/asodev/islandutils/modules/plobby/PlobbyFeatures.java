package net.asodev.islandutils.modules.plobby;

import net.asodev.islandutils.IslandUtilsClient;
import net.asodev.islandutils.IslandUtilsEvents;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.util.Sidebar;
import net.asodev.islandutils.util.Utils;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlobbyFeatures {
    public static long lastCopy = 0;
    private static final Component copiedMessage = Component.literal("Copied code to clipboard!")
            .withStyle(Style.EMPTY.withColor(TextColor.parseColor("#ffff00")));

    public static void registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!MccIslandState.isOnline()) return;
            if (!IslandUtilsClient.openPlobbyKey.consumeClick() || client.player == null) return;
            client.player.connection.sendCommand("plobby"); // Do /plobby to open the plobby menu
        });

        IslandUtilsEvents.CHAT_MESSAGE.register((state, modify) -> {
            if ((System.currentTimeMillis() - lastCopy) > 5000) return; // If we copied the code less than 5s ago
            modify.replace(copiedMessage); // Replace with the copy success message
            lastCopy = 0; // Reset the copy time
        });
    }

    private final static Pattern codePattern = Pattern.compile(".•.([A-Za-z]{2}\\d{4})");
    public static String getJoinCodeFromItem(ItemStack item) {
        List<Component> lores = Utils.getLores(item);
        for (Component lore : lores) {
            String loreString = lore.getString();
            Matcher matcher = codePattern.matcher(loreString);
            if (!matcher.find()) continue;
            return matcher.group(1);
        }
        return null;
    }

    private static final Pattern plobbySidebarLine = Pattern.compile("PLOBBY.\\(.");
    public static boolean isInPlobby() {
        Component sidebarName = Sidebar.getSidebarName();
        if (!MccIslandState.isOnline() || sidebarName == null) return false;

        int lineNumber = Sidebar.findLine((line) -> plobbySidebarLine.matcher(line.getString()).find());
        boolean nameContainsPlobby = sidebarName.getString().contains("(Plobby)");
        return lineNumber != -1 || nameContainsPlobby;
    }

    public static boolean isPlobbyOwner() {
        List<Component> lines = Sidebar.getSidebarLines();
        int plobbyHeaderLine = Sidebar.findLine((line) -> plobbySidebarLine.matcher(line.getString()).find(), lines);
        Component line = Sidebar.getLine(lines, plobbyHeaderLine + 1);
        if (line == null) return false;
        return line.getString().toLowerCase().contains(Minecraft.getInstance().getUser().getName().toLowerCase());
    }

}

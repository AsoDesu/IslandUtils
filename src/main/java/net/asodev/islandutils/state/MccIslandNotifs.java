package net.asodev.islandutils.state;

import com.mojang.realmsclient.Unit;
import net.asodev.islandutils.IslandUtils;
import net.asodev.islandutils.modules.crafting.state.CraftingItem;
import net.asodev.islandutils.modules.crafting.state.CraftingItems;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.options.categories.CraftingOptions;
import net.asodev.islandutils.util.resourcepack.ResourcePackUpdater;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;

public class MccIslandNotifs {
    private static final Component completedCrafts = Component.translatable("islandutils.message.crafting.completedCrafts").setStyle(Style.EMPTY.withBold(true).withColor(ChatFormatting.WHITE));

    public static List<Component> getNotifLines() {
        List<Component> components = new ArrayList<>();

        MccIslandNotifs.addCraftingNotifs(components);
        MccIslandNotifs.addPackDownloadNotifs(components);

        return components;
    }

    private static void addCraftingNotifs(List<Component> components) {
        List<Component> craftingLists = new ArrayList<>();
        boolean anycomplete = false;
        CraftingOptions options = IslandOptions.getCrafting();
        if (options.isEnableCraftingNotifs() && options.isNotifyServerList()) { // "i'm a never-nester"
            for (CraftingItem item : CraftingItems.getItems()) {
                if (!item.isComplete()) continue;
                craftingLists.add(Component.literal("  ").append(item.getTitle()));
                anycomplete = true;
            }
        }
        if (!anycomplete) return;

        components.add(completedCrafts);
        components.addAll(craftingLists);
    }

    private static void addPackDownloadNotifs(List<Component> components) {
        ResourcePackUpdater.PackDownloadListener currentDownload = IslandUtils.packUpdater.currentDownload;
        if (currentDownload == null) return;

        MutableComponent downloadTitle = Component.translatable("islandutils.message.music.downloadingMusic")
                .setStyle(Style.EMPTY.withBold(true));

        String bytesText = Unit.humanReadable(currentDownload.getBytesDownloaded());
        OptionalLong size = currentDownload.getSize();
        String maxSize = size.isPresent() ? " / " + Unit.humanReadable(size.getAsLong()) : "";
        Component downloadProgress = Component.literal(" " + bytesText + maxSize);

        components.add(downloadTitle);
        components.add(downloadProgress);
    }

}

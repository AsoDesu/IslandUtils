package net.asodev.islandutils.state;

import net.asodev.islandutils.state.crafting.state.CraftingItem;
import net.asodev.islandutils.state.crafting.state.CraftingItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MccIslandNotifs {
    private static Component completedCrafts = Component.literal("Completed Crafts:").setStyle(Style.EMPTY.withBold(true).withColor(ChatFormatting.WHITE));

    public static List<Component> getNotifLines() {
        List<Component> components = new ArrayList<>();

        List<Component> craftingLists = new ArrayList<>();
        boolean anycomplete = false;
        for (CraftingItem item : CraftingItems.getItems()) {
            if (item.isComplete()) {
                craftingLists.add(Component.literal("  ").append(item.getTitle()));
                anycomplete = true;
            }
        }

        if (anycomplete) {
            components.add(completedCrafts);
            components.addAll(craftingLists);
        }

        return components;
    }

}

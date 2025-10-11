package net.asodev.islandutils.modules.crafting;

import net.asodev.islandutils.modules.crafting.state.CraftingItem;
import net.asodev.islandutils.modules.crafting.state.CraftingItems;
import net.asodev.islandutils.util.ChatUtils;
import net.asodev.islandutils.util.TimeUtil;
import net.asodev.islandutils.util.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class CraftingUI {
    public static Style CHEST_BACKGROUND_STYLE = Style.EMPTY.withColor(ChatFormatting.WHITE).withFont(ResourceLocation.fromNamespaceAndPath("mcc", "chest_backgrounds"));

    private static Component assemblerComponent;
    private static Component forgeComponent;

    public static CraftingMenuType craftingMenuType(Component component) {
        if (assemblerComponent == null || forgeComponent == null) return null;

        if (component.contains(assemblerComponent)) return CraftingMenuType.ASSEMBLER;
        if (component.contains(forgeComponent)) return CraftingMenuType.FORGE;
        return null;
    }

    public static void analyseCraftingItem(CraftingMenuType type, ItemStack item, int slot) {
        if (!isInputSlot(slot)) {
            CraftingItems.removeSlot(type, slot);
            return;
        }

        List<Component> tooltipLines = Utils.getTooltipLines(item);
        if (tooltipLines != null && isActive(tooltipLines)) {
            String textLines = tooltipLines.stream().map(Component::getString).collect(Collectors.joining());
            var matcher = TimeUtil.TIME_REGEX.matcher(textLines);
            if (matcher.find()) {
                var time = matcher.group();
                long timeLeftSeconds = TimeUtil.getTimeSeconds(time);
                if(timeLeftSeconds == -1) {
                    ChatUtils.debug("Failed to parse time: " +time, ChatFormatting.RED);
                    return;
                }
                long timeLeft = TimeUtil.getTimeSeconds(time) + 60;
                long finishTimestamp = System.currentTimeMillis() + (timeLeft * 1000);

                CraftingItem craftingItem = new CraftingItem();
                craftingItem.setCraftingMenuType(type);
                craftingItem.setFinishesCrafting(finishTimestamp);
                craftingItem.setSlot(slot);
                craftingItem.setType(item.getItem());
                craftingItem.setTitle(item.getHoverName());
                craftingItem.setItemModel(item.getOrDefault(DataComponents.ITEM_MODEL, ResourceLocation.withDefaultNamespace("missingno")));

                CraftingItems.addItem(craftingItem);
                ChatUtils.debug("[#" + slot + " " + type.name() + "] Found active craft: " + item.getDisplayName().getString() + " (" + timeLeft + ")");
                return;
            }
        }

        CraftingItems.removeSlot(type, slot);
        ChatUtils.debug(type.name() + " - Found empty craft slot: " + slot + "!");
    }

    private static boolean isActive(List<Component> tooltipLines) {
        return tooltipLines.stream().anyMatch(p -> p.getString().contains("Shift-Click to Cancel"));
    }

    private static boolean isInputSlot(int slot) {
        return slot >= 19 && slot <= 23;
    }

    public static void setAssemblerCharacter(String assemblerCharacter) {
        CraftingUI.assemblerComponent = Component.literal(assemblerCharacter).withStyle(CHEST_BACKGROUND_STYLE);
    }

    public static void setForgeCharacter(String forgeCharacter) {
        CraftingUI.forgeComponent = Component.literal(forgeCharacter).withStyle(CHEST_BACKGROUND_STYLE);
    }
}

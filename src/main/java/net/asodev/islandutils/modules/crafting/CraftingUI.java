package net.asodev.islandutils.modules.crafting;

import net.asodev.islandutils.modules.crafting.state.CraftingItem;
import net.asodev.islandutils.modules.crafting.state.CraftingItems;
import net.asodev.islandutils.util.ChatUtils;
import net.asodev.islandutils.util.TimeUtil;
import net.asodev.islandutils.util.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class CraftingUI {
    private static Style CHEST_BACKGROUND_STYLE = Style.EMPTY.withColor(ChatFormatting.WHITE).withFont(new ResourceLocation("mcc", "chest_backgrounds"));
    private static Component assemblerComponent;
    private static Component forgeComponent;

    public static CraftingMenuType craftingMenuType(Component component) {
        if (component.contains(assemblerComponent)) return CraftingMenuType.ASSEMBLER;
        if (component.contains(forgeComponent)) return CraftingMenuType.FORGE;
        return null;
    }

    private static TextColor timeLeftColor = TextColor.fromLegacyFormat(ChatFormatting.RED);
    public static void analyseCraftingItem(CraftingMenuType type, ItemStack item, int slot) {
        if (!isInputSlot(slot)) return;

        List<Component> lores = Utils.getLores(item);
        if (lores != null && isActive(lores)) {
            String timeLeftString = null;
            for (Component line : lores) {
                TextColor color = line.getStyle().getColor();
                if (color != timeLeftColor) continue;
                timeLeftString = line.getString();
                break;
            }
            if (timeLeftString != null) {
                long timeLeft = TimeUtil.getTimeSeconds(timeLeftString) + 60;
                long finishTimestamp = System.currentTimeMillis() + (timeLeft * 1000);

                CraftingItem craftingItem = new CraftingItem();
                craftingItem.setCraftingMenuType(type);
                craftingItem.setFinishesCrafting(finishTimestamp);
                craftingItem.setSlot(slot);
                craftingItem.setType(item.getItem());
                craftingItem.setTitle(item.getHoverName());
                craftingItem.setCustomModelData(Utils.customModelData(item));

                CraftingItems.addItem(craftingItem);

                ChatUtils.debug("[#" + slot + " " + type.name() + "] Found active craft: " + item.getDisplayName().getString() + " (" + timeLeft + ")");
                return;
            }
        }

        CraftingItems.removeSlot(type, slot);
        ChatUtils.debug(type.name() + " - Found empty craft slot: " + slot + "!");
    }

    private static boolean isActive(List<Component> lores) {
        return lores.stream().anyMatch(p -> p.getString().contains("Shift-Click to Cancel"));
    }
    private static boolean isInputSlot(int slot) {
        return slot >= 37 && slot < 42;
    }

    public static void setAssemblerCharacter(String assemblerCharacter) {
        CraftingUI.assemblerComponent = Component.literal(assemblerCharacter).withStyle(CHEST_BACKGROUND_STYLE);
    }
    public static void setForgeCharacter(String forgeCharacter) {
        CraftingUI.forgeComponent = Component.literal(forgeCharacter).withStyle(CHEST_BACKGROUND_STYLE);
    }
}

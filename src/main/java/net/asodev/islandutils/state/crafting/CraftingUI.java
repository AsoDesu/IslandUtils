package net.asodev.islandutils.state.crafting;

import net.asodev.islandutils.state.crafting.state.CraftingItem;
import net.asodev.islandutils.state.crafting.state.CraftingItems;
import net.asodev.islandutils.util.ChatUtils;
import net.asodev.islandutils.util.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.ItemStack;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CraftingUI {

    private static String assemblerCharacter;
    private static String forgeCharacter;

    public static CraftingMenuType craftingMenuType(Component component) {
        String string = component.getString();
        if (string.contains(assemblerCharacter)) return CraftingMenuType.ASSEMBLER;
        if (string.contains(forgeCharacter)) return CraftingMenuType.FORGE;
        return null;
    }

    private static TextColor timeLeftColor = TextColor.fromLegacyFormat(ChatFormatting.RED);
    public static void analyseCraftingItem(CraftingMenuType type, ItemStack item, int slot) {
        if (!isInCraftingSlot(slot)) return;

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
                long timeLeft = getTimeLeft(timeLeftString);

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MINUTE, 1);
                calendar.set(Calendar.SECOND, 0);
                long currentMillis = calendar.getTimeInMillis();
                long finishTimestamp = currentMillis + (timeLeft * 1000);

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
    private static boolean isInCraftingSlot(int slot) {
        return slot >= 37 && slot < 42;
    }

    private static Map<String, Pattern> patternMap = Map.of(
            "hours", Pattern.compile("(\\d+)h"),
            "mins", Pattern.compile("(\\d+)m"),
            "seconds", Pattern.compile("(\\d+)s")
    );
    private static long getTimeLeft(String string) {
        long timeLeft = 0;
        for (Map.Entry<String, Pattern> entry : patternMap.entrySet()) {
            Matcher matcher = entry.getValue().matcher(string);
            if (!matcher.find()) continue;

            String stringValue = matcher.group(1);
            int value;
            try { value = Integer.parseInt(stringValue); }
            catch (Exception e) { continue; }

            switch (entry.getKey()) {
                case "hours" -> timeLeft += (value * 3600L);
                case "mins"  -> timeLeft += (value * 60L);
                case "seconds"  -> timeLeft += value;
            }
        }
        return timeLeft;
    }

    public static void setAssemblerCharacter(String assemblerCharacter) {
        CraftingUI.assemblerCharacter = assemblerCharacter;
    }
    public static void setForgeCharacter(String forgeCharacter) {
        CraftingUI.forgeCharacter = forgeCharacter;
    }
}

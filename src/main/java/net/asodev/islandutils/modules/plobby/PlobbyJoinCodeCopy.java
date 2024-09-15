package net.asodev.islandutils.modules.plobby;

import net.asodev.islandutils.IslandUtilsEvents;
import net.asodev.islandutils.util.ChatUtils;
import net.asodev.islandutils.util.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlobbyJoinCodeCopy {
    public static long lastCopy = 0;
    private static final Component copiedMessage = Component.translatable("islandutils.message.plobby.codeCopied")
            .withStyle(Style.EMPTY.withColor(ChatUtils.parseColor("#ffff00")));

    public static void register() {
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

}

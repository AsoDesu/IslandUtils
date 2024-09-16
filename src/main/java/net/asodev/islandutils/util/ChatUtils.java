package net.asodev.islandutils.util;

import net.asodev.islandutils.options.IslandOptions;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.regex.Pattern;

public class ChatUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger("IslandUtils");
    private static final Pattern MCC_HUD_UNSUPPORTED_SYMBOL_PATTERN = Pattern.compile("[^!-)*-9:-?@A-~‘’“”\\s]");
    public static final Style iconsFontStyle = Style.EMPTY.withColor(ChatFormatting.WHITE).withFont(ResourceLocation.fromNamespaceAndPath("island","icons"));
    public static final String prefix = "&b[&eIslandUtils&b]";
    public static String translate(String s) {
        return s.replaceAll("&", "§");
    }

    public static void send(String s) {
        send(Component.literal(translate(prefix + " " + s)));
    }

    public static void debug(String s, Object... args) {
        debug(String.format(s, args));
    }

    public static void debug(String s) {
        if (!IslandOptions.getMisc().isDebugMode()) {
            LOGGER.info("[DEBUG] {}", s);
            return;
        }
        send(Component.literal("[IslandUtils] " + s).withStyle(ChatFormatting.GRAY));
    }

    public static void sendWithPrefix(Component component) {
        send(Component.literal(translate(prefix + " ")).append(component));
    }
    
    public static void send(Component component) {
        Minecraft.getInstance().getChatListener().handleSystemMessage(component, false);
    }

    public static TextColor parseColor(String hex) {
        Optional<TextColor> result = TextColor.parseColor(hex).result();
        return result.orElse(null);
    }

    /**
     * Checks if the string has any unsupported symbols by the <code>mcc:hud</code> font.
     * <p> 
     * For example: 
     * <p>
     * <code>
     * checkForHubUnsupportedSymbols("abcd ї efgh") == false;
     * checkForHubUnsupportedSymbols("abcdEFGH") == true;
     * </code>
     * @param s The string to perform the check on.
     * @return Returns true if there are no unsupported symbols,
     *         or false if there's at least one unsupported symbol.
     */
    public static boolean checkForHudUnsupportedSymbols(String s) {
        // this hellish abomination of a regex matches any symbols, 
        // that are not supported by the mcc:hud font, thus should use the default font.
        return !MCC_HUD_UNSUPPORTED_SYMBOL_PATTERN.matcher(s).find();
    }
}

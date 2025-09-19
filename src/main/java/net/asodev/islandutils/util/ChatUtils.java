package net.asodev.islandutils.util;

import net.asodev.islandutils.options.IslandOptions;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class ChatUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger("IslandUtils");

    public static final String CHAT_PREFIX = "&b[&eIslandUtils&b]";


    public static String translate(String s) {
        return s.replaceAll("&", "§");
    }

    public static void send(String s) {
        send(Component.literal(translate(CHAT_PREFIX + " " + s)));
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

    public static void send(Component component) {
        Minecraft.getInstance().getChatListener().handleSystemMessage(component, false);
    }

    public static TextColor parseColor(String hex) {
        Optional<TextColor> result = TextColor.parseColor(hex).result();
        return result.orElse(null);
    }

    public ChatUtils() {
    }
}

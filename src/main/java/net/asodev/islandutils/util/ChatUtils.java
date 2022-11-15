package net.asodev.islandutils.util;

import net.asodev.islandutils.options.IslandOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.chat.Component;

public class ChatUtils {

    public static final String prefix = "&b[&eIslandUtils&b]";
    public static String translate(String s) {
        return s.replaceAll("&", "§");
    }

    public static void dev(String s) {
        send(Component.literal(translate(prefix + " " + s)));
    }

    public static void debug(String s, Object... args) {
        debug(String.format(s, args));
    }

    public static void debug(String s) {
        if (!IslandOptions.getOptions().isDebugMode()) return;
        send(Component.literal(translate("&7[IslandUtils] " + s)));
    }

    public static void send(Component component) {
        Minecraft.getInstance().getChatListener().handleSystemMessage(component, false);
    }

}

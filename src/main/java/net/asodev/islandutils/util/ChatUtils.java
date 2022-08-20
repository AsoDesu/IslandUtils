package net.asodev.islandutils.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.chat.Component;

public class ChatUtils {

    public static final String prefix = "&b[&eIslandUtils&b]";
    public static String translate(String s) {
        return s.replaceAll("&", "§");
    }

    public static void dev(String s) {
        Minecraft.getInstance().getChatListener().handleSystemMessage(Component.literal(translate(prefix + " " + s)), false);
    }

}

package net.asodev.islandutils.util;

import net.asodev.islandutils.options.IslandOptions;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

public class ChatUtils {

    public static final Style iconsFontStyle = Style.EMPTY.withColor(ChatFormatting.WHITE).withFont(new ResourceLocation("island","icons"));
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
        if (!IslandOptions.getMisc().isDebugMode()) return;
        send(Component.literal(translate("&7[IslandUtils] " + s)));
    }

    public static void debug(Component component) {
        if (!IslandOptions.getMisc().isDebugMode()) return;
        send(Component.literal(translate("&7[IslandUtils] ")).append(component));
    }

    public static void send(Component component) {
        Minecraft.getInstance().getChatListener().handleSystemMessage(component, false);
    }

}

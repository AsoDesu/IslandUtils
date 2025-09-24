package net.asodev.islandutils.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

public class FontUtils {

    public static final ResourceLocation MCC_ICONS_FONT = ResourceLocation.fromNamespaceAndPath("island", "mcc_icons");
    public static final ResourceLocation CUSTOM_ICONS_FONT = ResourceLocation.fromNamespaceAndPath("island", "custom_icons");

    public static final Style MCC_ICONS_STYLE = Style.EMPTY.withColor(ChatFormatting.WHITE).withFont(MCC_ICONS_FONT);
    public static final Style CUSTOM_ICONS_STYLE = Style.EMPTY.withColor(ChatFormatting.WHITE).withFont(CUSTOM_ICONS_FONT);

    public static Component CRAFTING = Component.literal("\ue003").withStyle(MCC_ICONS_STYLE);
    public static Component FUSION_CRAFTING = Component.literal("\ue002").withStyle(MCC_ICONS_STYLE);

    public static Component ICON_SOCIAL = Component.literal("\ue001").withStyle(MCC_ICONS_STYLE);
    public static Component ICON_TICK_SMALL = Component.literal("\ue005").withStyle(MCC_ICONS_STYLE);
    public static Component ICON_MIDDLE_CLICK = Component.literal("\ue004").withStyle(CUSTOM_ICONS_STYLE);

    public static Component CHAT_CHANNEL_LOCAL = Component.literal("\ue001").withStyle(CUSTOM_ICONS_STYLE);
    public static Component CHAT_CHANNEL_PARTY = Component.literal("\ue002").withStyle(CUSTOM_ICONS_STYLE);
    public static Component CHAT_CHANNEL_TEAM = Component.literal("\ue003").withStyle(CUSTOM_ICONS_STYLE);
    public static Component CHAT_CHANNEL_PLOBBY = Component.literal("\ue006").withStyle(CUSTOM_ICONS_STYLE);

    public static Component TOOLTIP_HAT = Component.literal("\ue007").withStyle(MCC_ICONS_STYLE);
    public static Component TOOLTIP_ACCESSORY = Component.literal("\ue008").withStyle(MCC_ICONS_STYLE);
    public static Component TOOLTIP_HAIR = Component.literal("\ue009").withStyle(MCC_ICONS_STYLE);

    private FontUtils() {
    }

}

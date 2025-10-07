package net.asodev.islandutils.util;

import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.util.updater.schema.AvailableUpdate;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Optional;
import java.util.regex.Pattern;

public class ChatUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger("IslandUtils");
    private static final Pattern MCC_HUD_UNSUPPORTED_SYMBOL_PATTERN = Pattern.compile("[^!-)*-9:-?@A-~‘’“”\\s]");
    public static final String CHAT_PREFIX = "&b[&eIslandUtils&b]";

    public static final int UPDATE_AVAILABLE_COLOR = TextColor.parseColor("#d1ffab").getOrThrow().getValue();
    public static final int PRE_RELEASE_COLOR = TextColor.parseColor("#ff9191").getOrThrow().getValue();
    public static final int UPDATE_NAME_COLOR = TextColor.parseColor("#fffbab").getOrThrow().getValue();

    private static MutableComponent getUpdatePrefix(ChatFormatting color) {
        return Component.literal("[").withStyle(color).append(FontUtils.ICON_UPGRADE).append(Component.literal("] ").withStyle(color));
    }

    public static String translate(String s) {
        return s.replaceAll("&", "§");
    }

    public static void send(String s) {
        send(Component.literal(translate(CHAT_PREFIX + " " + s)));
    }

    public static void debug(String s, Object... args) {
        debug(String.format(s, args));
    }

    public static void sendUpdateNotification(AvailableUpdate availableUpdate) {

        var versionComponent = Component.literal(availableUpdate.title()).withColor(UPDATE_NAME_COLOR).withStyle(ChatFormatting.UNDERLINE).withStyle(Style.EMPTY.withClickEvent(new ClickEvent.OpenUrl(URI.create(availableUpdate.releaseUrl()))));
        var linkStyle = Style.EMPTY.withClickEvent(new ClickEvent.OpenUrl(URI.create(availableUpdate.releaseUrl()))).withUnderlined(true).withHoverEvent(new HoverEvent.ShowText(Component.literal("Click to open in browser!").withStyle(ChatFormatting.GRAY)));
        var linkComponent = Component.literal(availableUpdate.releaseUrl()).withStyle(linkStyle);

        send(Component.empty());
        send(getUpdatePrefix(ChatFormatting.GREEN).append(Component.translatable("islandutils.message.core.updateAvailable")).withColor(UPDATE_AVAILABLE_COLOR).append(versionComponent));
        send(getUpdatePrefix(ChatFormatting.GREEN).append(Component.translatable("islandutils.message.core.updateAvailable.download")).withColor(UPDATE_AVAILABLE_COLOR).append(linkComponent));
        send(Component.empty());
    }

    public static void sendPreReleaseWarning() {
        send(Component.empty());
        send(getUpdatePrefix(ChatFormatting.RED).append(Component.translatable("islandutils.message.core.preReleaseWarn").withColor(PRE_RELEASE_COLOR)));
        send(Component.empty());

    }

    public static void debug(String s) {
        if (!IslandOptions.getMisc().isDebugMode()) {
            LOGGER.info("[DEBUG] {}", s);
            return;
        }
        send(Component.literal("[IslandUtils] " + s).withStyle(ChatFormatting.GRAY));
    }

    public static void sendWithPrefix(Component component) {
        send(Component.literal(translate(CHAT_PREFIX + " ")).append(component));
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
     *
     * @param s The string to perform the check on.
     * @return Returns true if there are no unsupported symbols,
     * or false if there's at least one unsupported symbol.
     */
    public static boolean checkForHudUnsupportedSymbols(String s) {
        // this hellish abomination of a regex matches any symbols,
        // that are not supported by the mcc:hud font, thus should use the default font.
        return !MCC_HUD_UNSUPPORTED_SYMBOL_PATTERN.matcher(s).find();
    }

    private ChatUtils() {
    }
}

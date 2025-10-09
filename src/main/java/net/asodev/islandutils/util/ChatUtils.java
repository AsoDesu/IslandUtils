package net.asodev.islandutils.util;

import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.util.updater.schema.AvailableUpdate;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.*;
import org.jetbrains.annotations.Nullable;
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
    public static final int PRE_RELEASE_COLOR_CHANNEL = TextColor.parseColor("#ff9191").getOrThrow().getValue();
    public static final int PRE_RELEASE_COLOR = TextColor.parseColor("#ff5959").getOrThrow().getValue();
    public static final int UPDATE_NAME_COLOR = TextColor.parseColor("#fffbab").getOrThrow().getValue();
    public static final int UPDATE_CLICK_COLOR = TextColor.parseColor("#abdbff").getOrThrow().getValue();

    private static MutableComponent getUpdatePrefix(AvailableUpdate.VersionState type) {
        Component icon;
        int color;
        if (type == AvailableUpdate.VersionState.UPDATE) {
            icon = FontUtils.ICON_UPGRADE;
            color = UPDATE_AVAILABLE_COLOR;
        } else {
            icon = FontUtils.ICON_WARNING;
            color = PRE_RELEASE_COLOR;
        }


        return Component.literal("[").withColor(color).append(icon).append(Component.literal("] ").withColor(color));
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

    public static void sendVersionStateMessage(@Nullable AvailableUpdate availableUpdate) {
        if (availableUpdate != null) sendUpdateNotification(availableUpdate);
        else sendPreReleaseWarning();
    }

    private static void sendUpdateNotification(AvailableUpdate availableUpdate) {

        var hoverEvent = new HoverEvent.ShowText(Component.literal("Click to open in browser!").withStyle(ChatFormatting.GRAY));
        var clickEvent = Style.EMPTY.withClickEvent(new ClickEvent.OpenUrl(URI.create(availableUpdate.releaseUrl()))).withHoverEvent(hoverEvent);

        var versionComponent = Component.literal(availableUpdate.title()).withColor(UPDATE_NAME_COLOR).withStyle(clickEvent);

        send(Component.empty());
        send(getUpdatePrefix(AvailableUpdate.VersionState.UPDATE).append(Component.translatable("islandutils.message.core.updateAvailable")).withColor(UPDATE_AVAILABLE_COLOR).append(versionComponent));
        send(getUpdatePrefix(AvailableUpdate.VersionState.UPDATE).append(Component.translatable("islandutils.message.core.updateAvailable.download").withColor(UPDATE_CLICK_COLOR).withStyle(ChatFormatting.UNDERLINE).withStyle(clickEvent)));
        send(Component.empty());
    }

    private static void sendPreReleaseWarning() {
        var channel = Component.literal("#test-feedback").withColor(PRE_RELEASE_COLOR_CHANNEL);
        send(Component.empty());
        send(getUpdatePrefix(AvailableUpdate.VersionState.PRE_RELEASE).append(Component.translatable("islandutils.message.core.preReleaseWarn").withColor(PRE_RELEASE_COLOR)).append(channel));
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

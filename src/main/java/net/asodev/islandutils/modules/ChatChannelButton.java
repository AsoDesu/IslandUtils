package net.asodev.islandutils.modules;

import net.asodev.islandutils.modules.plobby.PlobbyFeatures;
import net.asodev.islandutils.state.MccIslandState;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.asodev.islandutils.util.ChatUtils.iconsFontStyle;

/**
 * A button that appears in the chat screen to select a chat channel.
 *
 * @param name the channel's name, as passed to the command {@code /channel <name>}
 * @param text the button's text contents, rendered without a background or shadow
 */
public record ChatChannelButton(
        String name,
        Component text
) {
    private static final ChatChannelButton LOCAL = new ChatChannelButton(
            "local",
            Component.literal("\ue002").withStyle(iconsFontStyle)
    );

    private static final ChatChannelButton PARTY = new ChatChannelButton(
            "party",
            Component.literal("\ue003").withStyle(iconsFontStyle)
    );

    private static final ChatChannelButton TEAM = new ChatChannelButton(
            "team",
            Component.literal("\ue004").withStyle(iconsFontStyle)
    );

    private static final ChatChannelButton PLOBBY = new ChatChannelButton(
            "plobby",
            Component.literal("\ue011").withStyle(iconsFontStyle)
    );

    /**
     * Gets an immutable list of the buttons to add to the chat screen at this point in time.
     */
    public static List<ChatChannelButton> currentButtons() {
        final var channels = new ArrayList<ChatChannelButton>();

        channels.add(LOCAL);
        channels.add(PARTY);

        if (MccIslandState.getGame().hasTeamChat()) {
            channels.add(TEAM);
        }

        if (PlobbyFeatures.isInPlobby()) {
            channels.add(PLOBBY);
        }

        return Collections.unmodifiableList(channels);
    }
}

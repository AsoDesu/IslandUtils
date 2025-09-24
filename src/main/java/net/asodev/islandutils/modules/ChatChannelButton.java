package net.asodev.islandutils.modules;

import net.asodev.islandutils.modules.plobby.PlobbyFeatures;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.util.FontUtils;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    private static final ChatChannelButton LOCAL = new ChatChannelButton("local", FontUtils.CHAT_CHANNEL_LOCAL);
    private static final ChatChannelButton PARTY = new ChatChannelButton("party", FontUtils.CHAT_CHANNEL_PARTY);
    private static final ChatChannelButton TEAM = new ChatChannelButton("team", FontUtils.CHAT_CHANNEL_TEAM);
    private static final ChatChannelButton PLOBBY = new ChatChannelButton("plobby", FontUtils.CHAT_CHANNEL_PLOBBY);

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
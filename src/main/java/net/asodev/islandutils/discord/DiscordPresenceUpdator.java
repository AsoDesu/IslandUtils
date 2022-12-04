package net.asodev.islandutils.discord;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.activity.Activity;
import de.jcm.discordgamesdk.activity.ActivityType;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.state.STATE;

import java.time.Instant;
import java.util.UUID;

public class DiscordPresenceUpdator {

    static Activity activity;
    public static UUID timeLeftBossbar = null;
    static Instant started;
    public static void create() {
        DiscordPresence.init();

        activity = new Activity();
        activity.setType(ActivityType.PLAYING);

        activity.assets().setLargeImage("mcci");
        activity.assets().setLargeText("play.mccisland.net");

        if (started == null) started = Instant.ofEpochMilli(System.currentTimeMillis());
        if (IslandOptions.getOptions().showTimeElapsed)
            activity.timestamps().setStart(started);
        updateActivity();
    }

    public static void updateTimeLeft(Long endTimestamp) {
        if (!IslandOptions.getOptions().showTimeRemaining || !IslandOptions.getOptions().showGame) return;

        if (endTimestamp != null) activity.timestamps().setEnd(Instant.ofEpochMilli(endTimestamp));
        else activity.timestamps().setEnd(Instant.ofEpochSecond(0));
        updateActivity();
    }

    public static int lastLevel = -1;
    public static void setLevel(int level) {
        if (!IslandOptions.getOptions().showFactionLevel) return;

        lastLevel = level;
        String faction = "";
        if (MccIslandState.getFaction() != null)
            faction = " (" + MccIslandState.getFaction().getTitle() + ")";

        activity.assets().setSmallText("Level " + level + faction);
    }

    public static void updatePlace() {
        if (!IslandOptions.getOptions().showGame) return;

        activity.assets().setLargeImage(MccIslandState.getGame().name().toLowerCase());
        activity.assets().setLargeText(MccIslandState.getGame().getName());
        activity.assets().setSmallImage("mcci");

        if (MccIslandState.getGame() != STATE.HUB)
            activity.setDetails("Playing " + MccIslandState.getGame().getName());
        else {
            activity.setDetails("In the Hub");
            REMAIN_STATE = null;
            ROUND_STATE = null;
            activity.setState("");
            activity.timestamps().setEnd(Instant.ofEpochSecond(0));
        }

        if (MccIslandState.getGame() == STATE.TGTTOS || MccIslandState.getGame() == STATE.BATTLE_BOX)
            if (ROUND_STATE != null) roundScoreboardUpdate(ROUND_STATE, false);
        if (MccIslandState.getGame() == STATE.HITW || MccIslandState.getGame() == STATE.SKY_BATTLE)
            if (REMAIN_STATE != null) remainScoreboardUpdate(REMAIN_STATE, false);


        updateActivity();
    }

    static String REMAIN_STATE;
    public static void remainScoreboardUpdate(String value, Boolean set) {
        if (!IslandOptions.getOptions().showGameInfo || !IslandOptions.getOptions().showGame) return;

        if (set) REMAIN_STATE = "Remaining: " + value;
        if (MccIslandState.getGame() != STATE.HITW && MccIslandState.getGame() != STATE.SKY_BATTLE) return;
        activity.setState(REMAIN_STATE);
        updateActivity();
    }
    static String ROUND_STATE;
    public static void roundScoreboardUpdate(String value, Boolean set) {
        if (!IslandOptions.getOptions().showGameInfo || !IslandOptions.getOptions().showGame) return;

        if (set) ROUND_STATE = "Round: " + value;
        if (MccIslandState.getGame() != STATE.TGTTOS && MccIslandState.getGame() != STATE.BATTLE_BOX) return;
        activity.setState(ROUND_STATE);
        updateActivity();
    }

    public static void updateActivity() {
        if (!IslandOptions.getOptions().discordPresence) return;
        Core core = DiscordPresence.core;
        if (core == null || !core.isOpen()) {
            System.out.println("Discord is not initalised.");
            return;
        }
        core.activityManager().updateActivity(activity);
    }

    public static void clear() {
        DiscordPresence.clear();
    }

    public static void updateFromConfig(IslandOptions options) {
        if (activity == null || DiscordPresence.core == null) return;

        if (options.discordPresence) create();

        if (!options.showTimeElapsed) activity.timestamps().setStart(Instant.MAX);
        else activity.timestamps().setStart(started);

        if (!options.showTimeRemaining) activity.timestamps().setEnd(Instant.ofEpochSecond(0));

        if (!options.showGame) {
            activity.assets().setSmallImage("");
            activity.assets().setSmallText("");

            activity.assets().setLargeImage("mcci");
            activity.assets().setLargeText("play.mccisland.net");

            activity.setDetails("");
            activity.setState("");
            activity.timestamps().setEnd(Instant.ofEpochSecond(0));
        } else updatePlace();

        if (!options.showFactionLevel) activity.assets().setSmallText("");
        else setLevel(lastLevel);

        if (!options.showGameInfo) activity.setState("");
        else updatePlace();

        if (!options.discordPresence) clear();
    }

}

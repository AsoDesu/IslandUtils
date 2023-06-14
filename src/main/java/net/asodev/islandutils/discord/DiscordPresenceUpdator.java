package net.asodev.islandutils.discord;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.activity.Activity;
import de.jcm.discordgamesdk.activity.ActivityType;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.state.GAME;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.UUID;

public class DiscordPresenceUpdator {

    // EVERYTHING in this file that interacts with discord is in a try/catch
    // Discord GameSDK sometimes likes to not work whenever you ask it to do something
    // So we have to be sure we don't crash when that happens.

    @Nullable static Activity activity;
    public static UUID timeLeftBossbar = null;
    public static Instant started;
    public static void create() {
        if (!IslandOptions.getOptions().discordPresence) return;

        try {
            boolean didInit = DiscordPresence.init();
            if (!didInit) {
                System.out.println("Failed to start discord presence");
                return;
            }

            activity = new Activity();
            activity.setType(ActivityType.PLAYING);

            activity.assets().setLargeImage("mcci");
            activity.assets().setLargeText("play.mccisland.net");

            if (started == null) started = Instant.now();
            if (IslandOptions.getOptions().showTimeElapsed)
                activity.timestamps().setStart(started);

            updateActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateTimeLeft(Long endTimestamp) {
        if (activity == null) return;
        if (!IslandOptions.getOptions().showTimeRemaining || !IslandOptions.getOptions().showGame) return;

        try {
            if (endTimestamp != null) activity.timestamps().setEnd(Instant.ofEpochMilli(endTimestamp));
            else activity.timestamps().setEnd(Instant.ofEpochSecond(0));
        } catch (Exception e) { e.printStackTrace(); }
        updateActivity();
    }

    public static int lastLevel = -1;
    public static void setLevel(int level) {
        if (activity == null) return;
        if (!IslandOptions.getOptions().showFactionLevel) return;

        lastLevel = level;
        String faction = "";
        if (MccIslandState.getFaction() != null)
            faction = " (" + MccIslandState.getFaction().getTitle() + ")";

        try { activity.assets().setSmallText("Level " + level + faction); }
        catch (Exception e) { e.printStackTrace(); }
    }

    public static void updatePlace() {
        if (activity == null) return;
        if (!IslandOptions.getOptions().showGame) return;

        try {
            activity.assets().setLargeImage(MccIslandState.getGame().name().toLowerCase());
            activity.assets().setLargeText(MccIslandState.getGame().getName());
            activity.assets().setSmallImage("mcci");

            if (MccIslandState.getGame() != GAME.HUB)
                activity.setDetails("Playing " + MccIslandState.getGame().getName());
            else {
                activity.setDetails("In the Hub");
                REMAIN_STATE = null;
                ROUND_STATE = null;
                activity.setState("");
                activity.timestamps().setEnd(Instant.ofEpochSecond(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (MccIslandState.getGame() == GAME.TGTTOS || MccIslandState.getGame() == GAME.BATTLE_BOX)
            if (ROUND_STATE != null) roundScoreboardUpdate(ROUND_STATE, false);
        if (MccIslandState.getGame() == GAME.HITW || MccIslandState.getGame() == GAME.SKY_BATTLE)
            if (REMAIN_STATE != null) remainScoreboardUpdate(REMAIN_STATE, false);
        if (MccIslandState.getGame() == GAME.PARKOUR_WARRIOR)
            if (COURSE_STATE != null) courseScoreboardUpdate(COURSE_STATE, false);
        if (MccIslandState.getGame() == GAME.HUB) {
            activity.setState("");
        }

        updateActivity();
    }

    static String REMAIN_STATE;
    public static void remainScoreboardUpdate(String value, Boolean set) {
        if (activity == null) return;
        if (!IslandOptions.getOptions().showGameInfo || !IslandOptions.getOptions().showGame) return;

        if (set) REMAIN_STATE = "Remaining: " + value;
        if (MccIslandState.getGame() != GAME.HITW && MccIslandState.getGame() != GAME.SKY_BATTLE) return;

        try { activity.setState(REMAIN_STATE); }
        catch (Exception e) { e.printStackTrace(); }

        updateActivity();
    }
    static String ROUND_STATE;
    public static void roundScoreboardUpdate(String value, Boolean set) {
        if (activity == null) return;
        if (!IslandOptions.getOptions().showGameInfo || !IslandOptions.getOptions().showGame) return;

        if (set) ROUND_STATE = "Round: " + value;
        if (MccIslandState.getGame() != GAME.TGTTOS && MccIslandState.getGame() != GAME.BATTLE_BOX) return;

        try { activity.setState(ROUND_STATE); }
        catch (Exception e) { e.printStackTrace(); }

        updateActivity();
    }

    static String COURSE_STATE;
    public static void courseScoreboardUpdate(String value, Boolean set) {
        if (activity == null) return;
        if (!IslandOptions.getOptions().showGameInfo || !IslandOptions.getOptions().showGame) return;

        if (set) COURSE_STATE = value;
        if (MccIslandState.getGame() != GAME.PARKOUR_WARRIOR) return;

        try { activity.setState(COURSE_STATE); }
        catch (Exception e) { e.printStackTrace(); }

        updateActivity();
    }

    public static void updateActivity() {
        if (activity == null) return;
        if (!IslandOptions.getOptions().discordPresence) return;
        Core core = DiscordPresence.core;
        if (core == null || !core.isOpen()) return;

        try { core.activityManager().updateActivity(activity); }
        catch (Exception e) { e.printStackTrace(); }
    }

    public static void clear() {
        DiscordPresence.clear();
    }

    public static void updateFromConfig(IslandOptions options) {
        try {
            if (!MccIslandState.isOnline()) { clear(); return; }

            if (options.discordPresence) create();
            else { clear(); return; }

            if (activity == null) activity = new Activity();

            if (!options.showTimeElapsed) activity.timestamps().setStart(Instant.MAX);
            else {
                if (started == null) started = Instant.now();
                activity.timestamps().setStart(started);
            }

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
        } catch (Exception e) { e.printStackTrace(); }
    }

}

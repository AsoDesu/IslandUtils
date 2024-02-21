package net.asodev.islandutils.discord;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.activity.Activity;
import de.jcm.discordgamesdk.activity.ActivityType;
import net.asodev.islandutils.IslandUtilsEvents;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.options.categories.DiscordOptions;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.state.Game;
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
    private static boolean bigRatMode = false;

    public static void create(boolean enableBigRat) {
        bigRatMode = enableBigRat;
        System.out.println("ENABLEBIGRAT: " + enableBigRat);
        if (!IslandOptions.getDiscord().discordPresence) return;

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
            if (IslandOptions.getDiscord().showTimeElapsed)
                activity.timestamps().setStart(started);

            updateActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void init() {
        IslandUtilsEvents.GAME_UPDATE.register(DiscordPresenceUpdator::updatePlace);
    }

    public static void updateTimeLeft(Long endTimestamp) {
        if (activity == null) return;
        if (!IslandOptions.getDiscord().showTimeRemaining || !IslandOptions.getDiscord().showGame) return;

        try {
            if (endTimestamp != null) activity.timestamps().setEnd(Instant.ofEpochMilli(endTimestamp));
            else activity.timestamps().setEnd(Instant.ofEpochSecond(0));
        } catch (Exception e) { e.printStackTrace(); }
        updateActivity();
    }

    public static int lastLevel = -1;
    public static void setLevel(int level) {
        if (activity == null) return;
        if (!IslandOptions.getDiscord().showFactionLevel) return;

        lastLevel = level;
        String faction = "";
        if (MccIslandState.getFaction() != null)
            faction = " (" + MccIslandState.getFaction().getTitle() + ")";

        try { activity.assets().setSmallText("Level " + level + faction); }
        catch (Exception e) { e.printStackTrace(); }
    }

    public static void updatePlace(Game game) {
        if (activity == null) return;
        if (!IslandOptions.getDiscord().showGame) return;

        try {
            activity.assets().setLargeImage(game.name().toLowerCase());
            activity.assets().setLargeText(game.getName());
            activity.assets().setSmallImage("mcci");

            if (game != Game.HUB)
                activity.setDetails("Playing " + game.getName());
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

        if (game == Game.TGTTOS || game == Game.BATTLE_BOX)
            if (ROUND_STATE != null) roundScoreboardUpdate(ROUND_STATE, false);
        if (game == Game.HITW || game == Game.SKY_BATTLE || game == Game.DYNABALL)
            if (REMAIN_STATE != null) remainScoreboardUpdate(REMAIN_STATE, false);
        if (game == Game.PARKOUR_WARRIOR_DOJO)
            if (COURSE_STATE != null) courseScoreboardUpdate(COURSE_STATE, false);
        if (game == Game.PARKOUR_WARRIOR_SURVIVOR)
            if (LEAP_STATE != null) leapScoreboardUpdate(LEAP_STATE, false);
        if (game == Game.HUB) {
            activity.setState("");
        }

        updateActivity();
    }

    static String REMAIN_STATE;
    public static void remainScoreboardUpdate(String value, Boolean set) {
        if (activity == null) return;
        if (!IslandOptions.getDiscord().showGameInfo || !IslandOptions.getDiscord().showGame) return;

        if (set) REMAIN_STATE = "Remaining: " + value;
        if (MccIslandState.getGame() != Game.HITW && MccIslandState.getGame() != Game.SKY_BATTLE && MccIslandState.getGame() != Game.DYNABALL) return;

        try { activity.setState(REMAIN_STATE); }
        catch (Exception e) { e.printStackTrace(); }

        updateActivity();
    }
    static String ROUND_STATE;
    public static void roundScoreboardUpdate(String value, Boolean set) {
        if (activity == null) return;
        if (!IslandOptions.getDiscord().showGameInfo || !IslandOptions.getDiscord().showGame) return;

        if (set) ROUND_STATE = "Round: " + value;
        if (MccIslandState.getGame() != Game.TGTTOS && MccIslandState.getGame() != Game.BATTLE_BOX) return;

        try { activity.setState(ROUND_STATE); }
        catch (Exception e) { e.printStackTrace(); }

        updateActivity();
    }

    static String COURSE_STATE;
    public static void courseScoreboardUpdate(String value, Boolean set) {
        if (activity == null) return;
        if (!IslandOptions.getDiscord().showGameInfo || !IslandOptions.getDiscord().showGame) return;

        if (set) COURSE_STATE = value;
        if (MccIslandState.getGame() != Game.PARKOUR_WARRIOR_DOJO) return;

        try { activity.setState(COURSE_STATE); }
        catch (Exception e) { e.printStackTrace(); }

        updateActivity();
    }

    static String LEAP_STATE;
    public static void leapScoreboardUpdate(String value, Boolean set) {
        if (activity == null) return;
        if (!IslandOptions.getDiscord().showGameInfo || !IslandOptions.getDiscord().showGame) return;

        if (set) LEAP_STATE = "Leap: " + value;
        if (MccIslandState.getGame() != Game.PARKOUR_WARRIOR_SURVIVOR) return;

        try { activity.setState(LEAP_STATE); }
        catch (Exception e) { e.printStackTrace(); }

        updateActivity();
    }

    private static void overrideActivityWithBigRat() {
        activity = new Activity();
        activity.setType(ActivityType.LISTENING);
        activity.assets().setLargeImage("bigrat");
        activity.assets().setLargeText("BIG RAT");
        activity.setState("BIG RAT BIG RAT BIG RAT");
        activity.setDetails("BIG RAT BIG RAT BIG RAT");
        activity.timestamps().setEnd(Instant.now().plusSeconds(86400));
    }

    public static void updateActivity() {
        if (activity == null) return;
        if (!IslandOptions.getDiscord().discordPresence) return;
        Core core = DiscordPresence.core;
        if (core == null || !core.isOpen()) return;

        if (bigRatMode) {
            overrideActivityWithBigRat();
        }

        try { core.activityManager().updateActivity(activity); }
        catch (Exception e) { e.printStackTrace(); }
    }

    public static void clear() {
        DiscordPresence.clear();
    }

    public static void updateFromConfig(DiscordOptions options) {
        try {
            if (!MccIslandState.isOnline()) { clear(); return; }

            if (options.discordPresence) create(bigRatMode);
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
            } else updatePlace(MccIslandState.getGame());

            if (!options.showFactionLevel) activity.assets().setSmallText("");
            else setLevel(lastLevel);

            if (!options.showGameInfo) activity.setState("");
            else updatePlace(MccIslandState.getGame());

            if (!options.discordPresence) clear();
        } catch (Exception e) { e.printStackTrace(); }
    }

}

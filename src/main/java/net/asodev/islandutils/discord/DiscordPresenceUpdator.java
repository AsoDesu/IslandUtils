package net.asodev.islandutils.discord;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.activity.Activity;
import de.jcm.discordgamesdk.activity.ActivityType;
import net.asodev.islandutils.IslandUtilsEvents;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.options.categories.DiscordOptions;
import net.asodev.islandutils.state.Game;
import net.minecraft.client.resources.language.I18n;
import net.asodev.islandutils.state.MccIslandState;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class DiscordPresenceUpdator {

    // EVERYTHING in this file that interacts with discord is in a try/catch
    // Discord GameSDK sometimes likes to not work whenever you ask it to do something
    // So we have to be sure we don't crash when that happens.

    // dear contributors:
    // i am sorry.

    @Nullable public static Activity activity;
    public static UUID timeLeftBossbar = null;
    public static Instant started;
    private static boolean bigRatMode = false;

    private static List<Game> roundGames = List.of(Game.TGTTOS, Game.BATTLE_BOX);
    private static List<Game> remainGames = List.of(Game.HITW, Game.SKY_BATTLE, Game.DYNABALL, Game.ROCKET_SPLEEF_RUSH);
    private static List<Game> courseGames = List.of(Game.PARKOUR_WARRIOR_DOJO);
    private static List<Game> leapGames = List.of(Game.PARKOUR_WARRIOR_SURVIVOR);

    public static void create(boolean enableBigRat) {
        bigRatMode = enableBigRat;
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

    public static void updatePlace(Game game) {
        if (activity == null) return;
        if (!IslandOptions.getDiscord().showGame) return;

        try {
            activity.assets().setLargeImage(game.name().toLowerCase());
            activity.assets().setLargeText(game.getName());
            activity.assets().setSmallImage("mcci");

            if (game == Game.FISHING) {
                FishingPresenceUpdator.updateFishingPlace();
                REMAIN_STATE = null;
                ROUND_STATE = null;
            } else if (game != Game.HUB)
                activity.setDetails(I18n.get("islandutils.discordPresence.details.playing", game.getName()));
            else {
                // This is here, so the old translations for '...details.inHub' from other contributors still work
                if (I18n.exists("islandutils.discordPresence.details.inThePlace"))
                    activity.setDetails(I18n.get("islandutils.discordPresence.details.inThePlace", I18n.get("islandutils.discordPresence.place.hub")));
                else
                    activity.setDetails(I18n.get("islandutils.discordPresence.details.inHub"));
                
                REMAIN_STATE = null;
                ROUND_STATE = null;
                activity.setState("");
                activity.timestamps().setEnd(Instant.ofEpochSecond(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (roundGames.contains(game))
            if (ROUND_STATE != null) roundScoreboardUpdate(ROUND_STATE, false);
        if (remainGames.contains(game))
            if (REMAIN_STATE != null) remainScoreboardUpdate(REMAIN_STATE, false);
        if (courseGames.contains(game))
            if (COURSE_STATE != null) courseScoreboardUpdate(COURSE_STATE, false);
        if (leapGames.contains(game))
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

        if (set) REMAIN_STATE = I18n.get("islandutils.discordPresense.state.remaining", value);
        if (!remainGames.contains(MccIslandState.getGame())) return;

        try { activity.setState(REMAIN_STATE); }
        catch (Exception e) { e.printStackTrace(); }

        updateActivity();
    }
    static String ROUND_STATE;
    public static void roundScoreboardUpdate(String value, Boolean set) {
        if (activity == null) return;
        if (!IslandOptions.getDiscord().showGameInfo || !IslandOptions.getDiscord().showGame) return;

        if (set) ROUND_STATE = I18n.get("islandutils.discordPresense.state.round", value);
        if (!roundGames.contains(MccIslandState.getGame())) return;

        try { activity.setState(ROUND_STATE); }
        catch (Exception e) { e.printStackTrace(); }

        updateActivity();
    }

    static String COURSE_STATE;
    public static void courseScoreboardUpdate(String value, Boolean set) {
        if (activity == null) return;
        if (!IslandOptions.getDiscord().showGameInfo || !IslandOptions.getDiscord().showGame) return;

        if (set) COURSE_STATE = value;
        if (!courseGames.contains(MccIslandState.getGame())) return;

        try { activity.setState(COURSE_STATE); }
        catch (Exception e) { e.printStackTrace(); }

        updateActivity();
    }

    static String LEAP_STATE;
    public static void leapScoreboardUpdate(String value, Boolean set) {
        if (activity == null) return;
        if (!IslandOptions.getDiscord().showGameInfo || !IslandOptions.getDiscord().showGame) return;

        if (set) LEAP_STATE = I18n.get("islandutils.discordPresense.state.leap", value);
        if (!leapGames.contains(MccIslandState.getGame())) return;

        try { activity.setState(LEAP_STATE); }
        catch (Exception e) { e.printStackTrace(); }

        updateActivity();
    }

    private static void overrideActivityWithBigRat() {
        activity = new Activity();
        activity.setType(ActivityType.PLAYING);
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

        activity.timestamps().setStart(started);

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

            if (!options.showGameInfo) activity.setState("");
            else updatePlace(MccIslandState.getGame());

            if (!options.discordPresence) clear();
        } catch (Exception e) { e.printStackTrace(); }
    }

}

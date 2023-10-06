package net.asodev.islandutils.modules.reporting;

import net.asodev.islandutils.modules.reporting.reportui.ReportOptionsScreen;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.util.SuggestionProvider;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import java.util.ArrayList;
import java.util.List;

public class ReportHandler {

    private static final List<String> REASONS = new ArrayList<>();
    private static final List<String> PLAYERS = new ArrayList<>();

    private static Runnable onUpdate = () -> {};

    private static final SuggestionProvider<String> PLAYER_PROVIDER = SuggestionProvider.ofString("/report ")
            .registerListener(players -> {
                PLAYERS.clear();
                PLAYERS.addAll(players);
                onUpdate.run();
            });
    private static final SuggestionProvider<String> REASONS_PROVIDER = SuggestionProvider.ofString("/report test ")
            .registerListener(reasons -> {
                REASONS.clear();
                REASONS.addAll(reasons);
                PLAYER_PROVIDER.send();
            });

    private static String deferredReport = null;

    /**
     * Loads all the required data for the report menu.
     * <br>
     * This method is asynchronous and will call the runnable when the all data is loaded.
     * <br>
     * Order of loading:
     * <ol>
     *     <li>Reasons</li>
     *     <li>Players</li>
     *     <li>Runnable</li>
     * </ol>
     * @param runnable The runnable to call when the data is loaded.
     */
    public static void load(Runnable runnable) {
        ReportHandler.onUpdate = runnable;
        REASONS_PROVIDER.send();
    }

    public static List<String> getReasons() {
        return REASONS;
    }

    public static List<String> getPlayers() {
        return PLAYERS;
    }

    /**
     * @return True if the reporting menu should be opened.
     */
    public static boolean shouldChangeReporting() {
        return MccIslandState.isOnline() && IslandOptions.getMisc().isShowReportMenu();
    }

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (deferredReport != null) {
                client.setScreen(new ReportOptionsScreen(deferredReport));
                deferredReport = null;
            }
        });
    }

    public static void open(String username) {
        load(() -> deferredReport = username);
    }
}

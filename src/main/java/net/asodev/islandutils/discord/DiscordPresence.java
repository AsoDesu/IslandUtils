package net.asodev.islandutils.discord;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.LogLevel;

public class DiscordPresence {
    
    static Core core;
    static boolean initialised = false;

    public static boolean init() {
        if (initialised) return true;

        try {
            clear(); // just in case :)

            CreateParams params = new CreateParams();
            params.setClientID(1027930697417101344L);
            params.setFlags(CreateParams.Flags.NO_REQUIRE_DISCORD);
            core = new Core(params);
            // By default, logging is set to verbose for some reason, causing a lot of spam in the logs
            core.setLogHook(LogLevel.WARN, Core.DEFAULT_LOG_HOOK);
            initialised = true;
        } catch (Exception e) {
            System.out.println("Failed to Initialise Discord Presence.");
            e.printStackTrace();
            return false;
        }
        new Thread(() -> {
            while (core != null && core.isOpen()) {
                core.runCallbacks();
                try { Thread.sleep(16); }
                catch (Exception e) { e.printStackTrace(); }
            }
            System.out.println("Core has closed. Core: " + core);
        }, "IslandUtils - Discord Callbacks").start();
        return true;
    }

    public static void clear() {
        if (core == null) return;
        if (!core.isOpen()) return;

        try { core.close(); }
        catch (Exception e) { e.printStackTrace(); }

        initialised = false;
        core = null;
    }

}

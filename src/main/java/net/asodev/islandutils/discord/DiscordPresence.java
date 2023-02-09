package net.asodev.islandutils.discord;

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class DiscordPresence {
    
    static Core core;
    static boolean initalised = false;

    static String os = System.getProperty("os.name").toLowerCase();

    public static void init() {
        if (initalised) return;

        File discordLibrary;
        try {
            discordLibrary = NativeLibrary.grabDiscordNative();
            File discordJNI = NativeLibrary.grabDiscordJNI();

            if (os.contains("windows")) System.load(discordLibrary.getAbsolutePath());
            System.load(discordJNI.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("Failed to grab Natives: " + e.getMessage());
            return;
        }

        Core.initDiscordNative(discordLibrary.getAbsolutePath());

        CreateParams params = new CreateParams();
        params.setClientID(1027930697417101344L);
        params.setFlags(CreateParams.Flags.NO_REQUIRE_DISCORD);

        try {
            core = new Core(params);
            initalised = true;
        } catch (Exception e) {
            System.out.println("Failed to Initialise Discord Presence.");
            return;
        }

        new Thread(() -> {
            while (core.isOpen()) {
                core.runCallbacks();
                try { Thread.sleep(16); }
                catch (Exception e) { e.printStackTrace(); }
            }
        }).start();
    }

    public static void clear() {
        if (core == null) return;
        if (!core.isOpen()) return;
        core.close();
        initalised = false;
    }

}

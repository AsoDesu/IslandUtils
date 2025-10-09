package net.asodev.islandutils;

import net.asodev.islandutils.modules.FriendsInGame;
import net.asodev.islandutils.modules.crafting.state.CraftingItems;
import net.asodev.islandutils.modules.crafting.state.CraftingNotifier;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.util.Scheduler;
import net.asodev.islandutils.util.resourcepack.ResourcePackUpdater;
import net.asodev.islandutils.util.updater.UpdateManager;
import net.asodev.islandutils.util.updater.schema.AvailableUpdate;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class IslandUtils implements ModInitializer {
    private static final Logger logger = LoggerFactory.getLogger(IslandUtils.class);
    public static UpdateManager updater = new UpdateManager();
    public static ResourcePackUpdater packUpdater = new ResourcePackUpdater();
    public static CraftingNotifier craftingNotifier = new CraftingNotifier();

    public static Version version;
    public static String versionString;
    public static AvailableUpdate availableUpdate;
    private static boolean isPreRelease = false;

    @Override
    public void onInitialize() {
        IslandOptions.init();
        FriendsInGame.init();

        Optional<ModContainer> container = FabricLoader.getInstance().getModContainer("islandutils");
        container.ifPresent(modContainer -> {
            version = modContainer.getMetadata().getVersion();
            versionString = version.getFriendlyString();
        });

        isPreRelease = versionString.contains("-pre") || FabricLoader.getInstance().isDevelopmentEnvironment();
        if (!versionString.contains("-pre")) {
            updater.runUpdateCheck();
        }

        try {
            CraftingItems.load();
        } catch (Exception e) {
            logger.error("Failed to load crafting items", e);
        }

        craftingNotifier.register();
        packUpdater.get();
        Scheduler.create();
    }

    public static boolean isPreRelease() {
        return isPreRelease;
    }
}

package net.asodev.islandutils;

import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.resourcepack.ResourcePackUpdater;
import net.asodev.islandutils.state.crafting.state.CraftingItems;
import net.asodev.islandutils.state.crafting.state.CraftingNotifier;
import net.asodev.islandutils.updater.UpdateManager;
import net.asodev.islandutils.updater.schema.AvailableUpdate;
import net.asodev.islandutils.util.Scheduler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class IslandUtils implements ModInitializer {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    public static UpdateManager updater;
    public static ResourcePackUpdater packUpdater;

    public static String version = "";
    public static AvailableUpdate availableUpdate;
    private static boolean isPreRelease = false;

    @Override
    public void onInitialize() {
        IslandOptions.init();

        Optional<ModContainer> container = FabricLoader.getInstance().getModContainer("islandutils");
        container.ifPresent(modContainer -> version = modContainer.getMetadata().getVersion().getFriendlyString());

        updater = new UpdateManager();
        if (!version.contains("-pre")) {
            updater.runUpdateCheck();
        } else {
            isPreRelease = true;
        }

        try {
            CraftingItems.load();
        } catch (Exception e) {
            logger.error("Failed to load crafting items", e);
        }
        new CraftingNotifier();

        packUpdater = new ResourcePackUpdater();
        packUpdater.get();

        Scheduler.create();
    }

    public static boolean isPreRelease() {
        return isPreRelease;
    }
}

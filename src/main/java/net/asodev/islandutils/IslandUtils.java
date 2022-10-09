package net.asodev.islandutils;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.resourcepack.ResourcePackUpdater;
import net.asodev.islandutils.updater.UpdateManager;
import net.asodev.islandutils.updater.schema.AvailableUpdate;
import net.asodev.islandutils.updater.schema.GithubRelease;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.Minecraft;

import java.util.Optional;
import java.util.logging.Logger;

public class IslandUtils implements ModInitializer {

    public static UpdateManager updater;
    public static ResourcePackUpdater packUpdater;

    public static String version = "";
    public static AvailableUpdate availableUpdate;
    @Override
    public void onInitialize() {
        AutoConfig.register(IslandOptions.class, GsonConfigSerializer::new);

        Optional<ModContainer> container = FabricLoader.getInstance().getModContainer("islandutils");
        container.ifPresent(modContainer -> version = modContainer.getMetadata().getVersion().getFriendlyString());

        updater = new UpdateManager();
        updater.runUpdateCheck();

        packUpdater = new ResourcePackUpdater();
        packUpdater.get();
    }
}

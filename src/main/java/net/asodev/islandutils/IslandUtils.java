package net.asodev.islandutils;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.updator.Updater;
import net.asodev.islandutils.updator.schema.AvailableUpdate;
import net.asodev.islandutils.updator.schema.GithubRelease;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.impl.FabricLoaderImpl;

import java.util.Objects;
import java.util.Optional;

public class IslandUtils implements ModInitializer {

    public static Updater updater;
    public static String version;

    public static AvailableUpdate availableUpdate;
    @Override
    public void onInitialize() {
        AutoConfig.register(IslandOptions.class, GsonConfigSerializer::new);

        updater = new Updater();

        Optional<ModContainer> container = FabricLoader.getInstance().getModContainer("islandutils");
        container.ifPresent(modContainer -> version = modContainer.getMetadata().getVersion().getFriendlyString());

        try {
            updater.checkForUpdates().thenAccept(res -> {
                GithubRelease release = res.get(0);
                if (release == null) return;
                if (!version.equals(release.getTagName())) {
                    availableUpdate = new AvailableUpdate(release.getName(), release.getTagName(), release.getHtmlUrl());
                }
            });
        } catch (Exception e) {}
    }
}

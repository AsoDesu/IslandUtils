package net.asodev.islandutils;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.updater.UpdateManager;
import net.asodev.islandutils.updater.schema.AvailableUpdate;
import net.asodev.islandutils.updater.schema.GithubRelease;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.util.Optional;

public class IslandUtils implements ModInitializer {

    public static UpdateManager updater;
    public static String version;

    public static AvailableUpdate availableUpdate;
    @Override
    public void onInitialize() {
        AutoConfig.register(IslandOptions.class, GsonConfigSerializer::new);

        updater = new UpdateManager();

        Optional<ModContainer> container = FabricLoader.getInstance().getModContainer("islandutils");
        container.ifPresent(modContainer -> version = modContainer.getMetadata().getVersion().getFriendlyString());

        try {
            updater.checkForUpdates().thenAccept(res -> {
                if (res == null) return;
                if (!version.equals(res.getTagName())) {
                    availableUpdate = new AvailableUpdate(res.getName(), res.getTagName(),
                            "https://modrinth.com/mod/island-utils/version/" + res.getTagName());
                }
            });
        } catch (Exception e) {}
    }
}

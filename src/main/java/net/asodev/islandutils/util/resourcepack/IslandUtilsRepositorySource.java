package net.asodev.islandutils.util.resourcepack;

import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.RepositorySource;

import java.util.function.Consumer;

public class IslandUtilsRepositorySource implements RepositorySource {
    @Override
    public void loadPacks(Consumer<Pack> consumer) {
        if (ResourcePackUpdater.pack != null) {
            consumer.accept(ResourcePackUpdater.pack);
        }
    }
}

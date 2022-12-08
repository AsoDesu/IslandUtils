package net.asodev.islandutils.resourcepack;

import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;

import java.util.function.Consumer;

public class IslandUtilsPackSource implements RepositorySource {
    @Override
    public void loadPacks(Consumer<Pack> consumer) {
        if (ResourcePackUpdater.pack != null) {
            consumer.accept(ResourcePackUpdater.pack);
        }
    }
}

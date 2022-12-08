package net.asodev.islandutils.resourcepack;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.PackSource;

public class IslandUtilsPackSource implements PackSource {

    @Override
    public boolean shouldAddAutomatically() {
        return true;
    }

    @Override
    public Component decorate(Component packName) {
        return Component.literal("Fabric mod").withStyle(ChatFormatting.GRAY);
    }
}

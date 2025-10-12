package net.asodev.islandutils.fontloader;

import net.asodev.islandutils.modules.crafting.CraftingUI;
import net.asodev.islandutils.modules.scavenging.Scavenging;
import net.asodev.islandutils.modules.splits.LevelTimer;
import net.asodev.islandutils.util.ChatUtils;
import net.asodev.islandutils.util.FontUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FontLoaderManager {

    private static final List<FontLoaderAsset> assetList = List.of(
            new FontLoaderAsset("_fonts/chest_backgrounds/body/blueprint_assembly.png", (ignored, s) -> CraftingUI.setAssemblerCharacter(s)),
            new FontLoaderAsset("_fonts/chest_backgrounds/body/fusion_forge.png", (ignored, s) -> CraftingUI.setForgeCharacter(s)),
            new FontLoaderAsset("_fonts/chest_backgrounds/body/scavenging.png", (ignored, s) -> Scavenging.setDustCharacter(s)),
            new FontLoaderAsset("_fonts/icon/silver.png", (ignored, s) -> Scavenging.setSilverCharacter(s)),
            new FontLoaderAsset("_fonts/icon/coin_small.png", (ignored, s) -> Scavenging.setCoinCharacter(s)),
            new FontLoaderAsset("_fonts/icon/medals.png", (ignored, s) -> LevelTimer.medalCharacter = s)
    );

    private static final List<FontLoaderAsset> fulfilledAssets = new ArrayList<>();

    public static List<FontLoaderAsset> getAllAssets() {
        return assetList;
    }

    public static List<FontLoaderAsset> getUnfulfilledAssets() {
        var copy = new java.util.ArrayList<>(List.copyOf(assetList));
        copy.removeAll(fulfilledAssets);
        return Collections.unmodifiableList(copy);
    }

    public static List<FontLoaderAsset> getFulfilledAssets() {
        return Collections.unmodifiableList(fulfilledAssets);
    }

    public static void warnAboutUnfulfilledAssets() {
        getUnfulfilledAssets().forEach((asset) -> {
            ChatUtils.debug("WARNING! " +asset.texturePath() + " has not been loaded properly", ChatFormatting.RED);
        });
    }

    public static void tryFulfillAsset(ResourceLocation resourceLocation, String character) {
        Component component = Component.literal(character).setStyle(FontUtils.MCC_ICONS_STYLE);
        String path = resourceLocation.getPath();

        assetList.forEach((asset) -> {
            if (Objects.equals(asset.texturePath(), path)) {
                asset.consumer().accept(component, character);
                fulfilledAssets.add(asset);
            }
        });
    }

    public static void reset() {
        fulfilledAssets.clear();
        ChatUtils.debug("Reset fulfilled font loader assets");
    }

    private FontLoaderManager() {
    }
}

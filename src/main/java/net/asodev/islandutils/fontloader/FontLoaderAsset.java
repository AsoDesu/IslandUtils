package net.asodev.islandutils.fontloader;

import net.minecraft.network.chat.Component;

import java.util.function.BiConsumer;

public record FontLoaderAsset(String texturePath, BiConsumer<Component, String> consumer) {

}

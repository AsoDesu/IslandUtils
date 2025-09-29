package net.asodev.islandutils;

import com.mojang.blaze3d.platform.InputConstants;
import net.asodev.islandutils.discord.DiscordPresenceUpdator;
import net.asodev.islandutils.modules.DisguiseKeybind;
import net.asodev.islandutils.modules.NoxesiumIntegration;
import net.asodev.islandutils.modules.music.MusicManager;
import net.asodev.islandutils.modules.plobby.PlobbyFeatures;
import net.asodev.islandutils.modules.plobby.PlobbyJoinCodeCopy;
import net.asodev.islandutils.modules.splits.SplitManager;
import net.asodev.islandutils.modules.splits.ui.SplitUI;
import net.asodev.islandutils.state.Game;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.util.ChatUtils;
import net.asodev.islandutils.util.IslandUtilsCommand;
import net.asodev.islandutils.util.Utils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.lwjgl.glfw.GLFW;

import java.net.URI;

@Environment(EnvType.CLIENT)
public class IslandUtilsClient implements ClientModInitializer {
    public static KeyMapping openPlobbyKey;
    public static KeyMapping disguiseKeyBind;

    @Override
    public void onInitializeClient() {
        openPlobbyKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.islandutils.plobbymenu",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                "category.islandutils.keys"
        ));
        disguiseKeyBind = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.islandutils.disguise",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_N,
                "category.islandutils.keys"
        ));
        SplitManager.init();
        DisguiseKeybind.registerDisguiseInput();
        PlobbyFeatures.registerEvents();
        IslandUtilsCommand.register();
        DiscordPresenceUpdator.init();
        PlobbyJoinCodeCopy.register();

        if (Utils.isLunarClient()) {
            SplitUI.setupFallbackRenderer();
        }
        new NoxesiumIntegration().init();
        MusicManager.init();
    }

    public static void onJoinMCCI(boolean isProduction) {
        System.out.println("Connected to MCCI!");
        if (IslandUtils.availableUpdate != null) {
            ChatUtils.sendWithPrefix(Component.translatable("islandutils.message.core.updateAvailable", IslandUtils.availableUpdate.title()));

            URI releaseUri = URI.create(IslandUtils.availableUpdate.releaseUrl());
            Style style = Style.EMPTY.withClickEvent(new ClickEvent.OpenUrl(releaseUri));
            Component link = Component.literal(IslandUtils.availableUpdate.releaseUrl()).setStyle(style);
            Component text = Component.translatable("islandutils.message.core.updateUrl").append(link);

            ChatUtils.sendWithPrefix(text);
        } else if (IslandUtils.isPreRelease()) {
            ChatUtils.sendWithPrefix(Component.translatable("islandutils.message.core.preReleaseWarn"));
        }

        DiscordPresenceUpdator.create(!isProduction);
        MccIslandState.setGame(Game.HUB);
        IslandUtilsEvents.JOIN_MCCI.invoker().onEvent();
    }
}

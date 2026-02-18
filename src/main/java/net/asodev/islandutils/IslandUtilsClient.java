package net.asodev.islandutils;

import com.mojang.blaze3d.platform.InputConstants;
import net.asodev.islandutils.discord.DiscordPresenceUpdator;
import net.asodev.islandutils.fontloader.FontLoaderManager;
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
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class IslandUtilsClient implements ClientModInitializer {
    public static KeyMapping openPlobbyKey;
    public static KeyMapping disguiseKeyBind;
    public static KeyMapping.Category keyCategory = new KeyMapping.Category(Identifier.fromNamespaceAndPath("islandutils", "keys"));

    @Override
    public void onInitializeClient() {
        openPlobbyKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.islandutils.plobbymenu",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                keyCategory
        ));
        disguiseKeyBind = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.islandutils.disguise",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_N,
                keyCategory
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
        MusicManager.init();

        IslandUtilsEvents.PACK_FINAL_RESULT.register((uuid, finalResult) -> FontLoaderManager.warnAboutUnfulfilledAssets());
    }

    public static void onJoinMCCI(boolean isProduction) {
        System.out.println("Connected to MCCI!");
        ChatUtils.sendVersionStateMessage(IslandUtils.availableUpdate);

        DiscordPresenceUpdator.create(!isProduction);
        MccIslandState.setGame(Game.HUB);
        IslandUtilsEvents.JOIN_MCCI.invoker().onEvent();
        FontLoaderManager.reset();
        MusicManager.stop();
    }
}

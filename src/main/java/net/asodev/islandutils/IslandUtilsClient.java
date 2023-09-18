package net.asodev.islandutils;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.asodev.islandutils.discord.DiscordPresenceUpdator;
import net.asodev.islandutils.modules.plobby.PlobbyFeatures;
import net.asodev.islandutils.modules.plobby.state.GlobalPlobbyState;
import net.asodev.islandutils.modules.splits.SplitManager;
import net.asodev.islandutils.util.ChatUtils;
import net.asodev.islandutils.modules.DisguiseKeybind;
import net.asodev.islandutils.util.IslandUtilsCommand;
import net.asodev.islandutils.util.MusicUtil;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;

import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

import org.lwjgl.glfw.GLFW;

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
        GlobalPlobbyState.register();
    }

    public static void onJoinMCCI() {
        System.out.println("Connected to MCCI!");
        if (IslandUtils.availableUpdate != null) {
            ChatUtils.send("Hey! Update " + IslandUtils.availableUpdate.title() + " is available for Island Utils!");

            Style style = Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, IslandUtils.availableUpdate.releaseUrl()));
            Component link = Component.literal(IslandUtils.availableUpdate.releaseUrl()).setStyle(style);
            Component text = Component.literal(ChatUtils.translate(ChatUtils.prefix + " Download Here: &f")).append(link);

            ChatUtils.send(text);
        } else if (IslandUtils.isPreRelease()) {
            ChatUtils.send("&cYou are using a pre-release version of IslandUtils! Expect things to be broken and buggy, and report to #test-feedback!");
        }
        DiscordPresenceUpdator.create();
        IslandUtilsEvents.JOIN_MCCI.invoker().onEvent();
    }

    public static class Commands {
        public static LiteralArgumentBuilder<?> resetMusic = net.minecraft.commands.Commands.literal("resetmusic").executes(ctx -> {
            MusicUtil.resetMusic(ctx);
            return 1;
        });
    }
}

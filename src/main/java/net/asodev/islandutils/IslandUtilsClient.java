package net.asodev.islandutils;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.asodev.islandutils.discord.DiscordPresenceUpdator;
import net.asodev.islandutils.util.ChatUtils;
import net.asodev.islandutils.util.MusicUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

@Environment(EnvType.CLIENT)
public class IslandUtilsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

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
            ChatUtils.send("&cYou are using a pre-release version of IslandUtils! Expect things to be broken & buggy, and report to #test-feedback!");
        }
        DiscordPresenceUpdator.create();
    }

    public static class Commands {
        public static LiteralArgumentBuilder<?> resetMusic = net.minecraft.commands.Commands.literal("resetmusic").executes(ctx -> {
            MusicUtil.resetMusic(ctx);
            return 1;
        });
    }
}

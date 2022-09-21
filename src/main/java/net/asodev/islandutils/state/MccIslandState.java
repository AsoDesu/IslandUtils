package net.asodev.islandutils.state;

import net.asodev.islandutils.util.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MccIslandState {

    private static STATE game = STATE.HUB;

    public static STATE getGame() {
        return game;
    }
    public static void setGame(STATE game) {
        MccIslandState.game = game;
    }

    public static boolean isOnline() {
        ServerData currentServer = Minecraft.getInstance().getCurrentServer();
        return currentServer != null && currentServer.ip.toLowerCase().contains("mccisland.net");
    }
}

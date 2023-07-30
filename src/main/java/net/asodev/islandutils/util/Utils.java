package net.asodev.islandutils.util;

import net.asodev.islandutils.resourcepack.ResourcePackOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.io.*;
import java.util.List;

public class Utils {
    public static final Style MCC_HUD_FONT = Style.EMPTY.withFont(new ResourceLocation("mcc", "hud"));

    public static List<Component> getLores(ItemStack item) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return null;
        return item.getTooltipLines(player, TooltipFlag.Default.NORMAL);
    }

    public static String readFile(File file) throws Exception {
        if (!file.exists()) return null;

        FileInputStream in = new FileInputStream(file);
        String json = new String(in.readAllBytes());
        in.close();
        return json;
    }

    public static void writeFile(File file, String data) throws IOException {
        if (!file.exists()) file.createNewFile();

        FileOutputStream out = new FileOutputStream(file);
        out.write(data.getBytes());
        out.close();
    }

    public static void assertIslandFolder() {
        File folder = new File(ResourcePackOptions.islandFolder);
        if (!folder.exists()) folder.mkdir();
    }

    public static int customModelData(ItemStack item) {
        CompoundTag itemTag = item.getTag();
        return itemTag == null ? 0 : itemTag.getInt("CustomModelData");
    }
}

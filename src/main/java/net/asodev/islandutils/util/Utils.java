package net.asodev.islandutils.util;

import net.asodev.islandutils.util.resourcepack.ResourcePackOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Utils {
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);
    public static final ExecutorService savingQueue = Executors.newFixedThreadPool(2);
    public static final Style MCC_HUD_FONT = Style.EMPTY.withFont(new ResourceLocation("mcc", "hud"));
    private static final List<String> NON_PROD_IP_HASHES = List.of(
            "0c932ffaa687c756c4616a24eb49389213519ea8d18e0d9bdfd2d335771c35c7",
            "7f0d15bbb2ffaee1bbf0d23e5746afb753333d590f71ff8a5a186d86c3e79dda"
    );

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
        File folder = ResourcePackOptions.islandFolder.toFile();
        if (!folder.exists()) folder.mkdir();
    }

    public static int customModelData(ItemStack item) {
        CompoundTag itemTag = item.getTag();
        return itemTag == null ? 0 : itemTag.getInt("CustomModelData");
    }

    public static ResourceLocation getCustomItemID(ItemStack item) {
        CompoundTag publicBukkitValues = item.getTagElement("PublicBukkitValues");
        if (publicBukkitValues == null) return null;
        String customItemId = publicBukkitValues.getString("mcc:custom_item_id");
        if (customItemId.equals("")) return null;
        return new ResourceLocation(customItemId);
    }

    public static boolean isProdMCCI(String hostname) {
        String hostnameHash = Utils.calculateSha256(hostname);
        return !NON_PROD_IP_HASHES.contains(hostnameHash);
    }

    public static String calculateSha256(String input) {
        String output = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(input.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            output = String.format("%064x", new BigInteger(1, digest));
        } catch (NoSuchAlgorithmException e) {
            logger.error("Failed to calculate SHA-256 for " + input);
        }
        return output;
    }
}

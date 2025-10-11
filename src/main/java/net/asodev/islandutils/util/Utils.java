package net.asodev.islandutils.util;

import net.asodev.islandutils.util.resourcepack.ResourcePackOptions;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
    public static final Style MCC_HUD_FONT = Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath("mcc", "hud"));
    public static final String BLANK_ITEM_ID = "island_interface.generic.blank";
    private static final List<String> NON_PROD_IP_HASHES = List.of(
            "e927084bb931f83eece6780afd9046f121a798bf3ff3c78a9399b08c1dfb1aec", // bigrat.mccisland.net easteregg/test ip
            "0c932ffaa687c756c4616a24eb49389213519ea8d18e0d9bdfd2d335771c35c7",
            "7f0d15bbb2ffaee1bbf0d23e5746afb753333d590f71ff8a5a186d86c3e79dda",
            "09445264a9c515c83fc5a0159bda82e25d70d499f80df4a2d1c2f7e2ae6af997"
    );

    public static @Nullable List<Component> getTooltipLines(ItemStack item) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return null;
        return item.getTooltipLines(Item.TooltipContext.EMPTY, player, TooltipFlag.Default.NORMAL);
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

    public static ResourceLocation getCustomItemID(ItemStack item) {
        var base = item.get(DataComponents.ITEM_MODEL);
        return base == null ? null : ResourceLocation.fromNamespaceAndPath(base.getNamespace(), base.getPath().replace("/", "."));
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
            logger.error("Failed to calculate SHA-256 for {}", input);
        }
        return output;
    }

    public static boolean isLunarClient() {
        return FabricLoader.getInstance().isModLoaded("ichor");
    }
}

package net.asodev.islandutils.util.resourcepack;

import com.google.gson.Gson;
import net.asodev.islandutils.util.resourcepack.schema.ResourcePack;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.util.HttpUtil;
import net.minecraft.world.flag.FeatureFlagSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static net.asodev.islandutils.util.ChatUtils.translate;

public class ResourcePackUpdater {
    public static final Logger logger = LoggerFactory.getLogger(ResourcePackUpdater.class);

    private static final String url = "https://raw.githubusercontent.com/AsoDesu/islandutils-assets/master/pack.json";
    private static final Component title = Component.literal(translate("Island Utils"));
    private static final Component desc = Component.literal(translate("&6Music Resources"));
    HttpClient client;
    Gson gson;

    public ProgressScreen state = null;
    public boolean getting = false;
    public boolean accepted = false;
    public static Pack pack;

    public ResourcePackUpdater() {
        client = HttpClient.newBuilder().build();
        gson = new Gson();
    }

    public void downloadAndApply() throws Exception {
        logger.info("Downloading resource pack...");

        state = new ProgressScreen(false);

        File file = ResourcePackOptions.packZip.toFile();
        CompletableFuture<?> future = HttpUtil.downloadTo(file, new URL(ResourcePackOptions.data.url), new HashMap<>(), 0xFA00000, state, Minecraft.getInstance().getProxy());
        future.thenAccept(obj -> {
            logger.info("Applying resource pack...");
            apply(file, true);
        });
    }

    public void apply(File file, Boolean save) {
        getting = false;
        state = null;
        int version = SharedConstants.getCurrentVersion().getPackVersion(PackType.CLIENT_RESOURCES);
        pack = Pack.create(
                "island_utils",
                title,
                true,
                (d) -> new FilePackResources("IslandUtils", file, true),
                new Pack.Info(desc, version, FeatureFlagSet.of()),
                PackType.CLIENT_RESOURCES,
                Pack.Position.BOTTOM,
                true,
                PackSource.BUILT_IN
        );

        if (save) {
            try { ResourcePackOptions.save(); }
            catch (IOException e) { System.err.println("Failed to save resource pack options"); }
        }
    }

    public void get() {
        File file = ResourcePackOptions.packZip.toFile();
        if (file.exists()) apply(file, false);

        logger.info("Requesting resource pack...");
        try {
            ResourcePack current = ResourcePackOptions.get();
            requestUpdate().thenAccept(rp -> {
                logger.info("Received Resource Pack: " + rp.rev);
                if (current != null && Objects.equals(current.rev, rp.rev) && file.exists()) {
                    logger.info("Resource pack has not changed. Not downloading!");
                    apply(file, false);
                    return;
                }
                ResourcePackOptions.data = rp;
                try {
                    getting = true;
                    downloadAndApply();
                } catch (Exception e) {
                    getting = false;
                    logger.error("Failed to download resource pack!", e);
                }
            });
        } catch (Exception e) {
            getting = false;
            logger.error("Failed to get IslandUtils resource pack info!", e);
        }
    }

    private CompletableFuture<ResourcePack> requestUpdate() throws Exception {
        CompletableFuture<ResourcePack> f = new CompletableFuture<>();
        HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();
        client.sendAsync(req, HttpResponse.BodyHandlers.ofString()).thenAccept(res -> {
            f.complete(gson.fromJson(res.body(), ResourcePack.class));
        });
        return f;
    }

}

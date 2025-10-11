package net.asodev.islandutils.util.resourcepack;

import com.google.gson.Gson;
import net.asodev.islandutils.IslandUtils;
import net.asodev.islandutils.util.resourcepack.schema.ResourcePack;
import net.minecraft.FileUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.util.HttpUtil;
import net.minecraft.world.flag.FeatureFlagSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.concurrent.CompletableFuture;

import static net.asodev.islandutils.util.ChatUtils.translate;

public class ResourcePackUpdater {
    public static final Logger logger = LoggerFactory.getLogger(ResourcePackUpdater.class);

    private static final String url_release = "https://raw.githubusercontent.com/AsoDesu/islandutils-assets/master/pack.json";
    private static final String url_pre = "https://raw.githubusercontent.com/AsoDesu/islandutils-assets/master/pack_beta.json";
    private static final Component title = Component.literal(translate("Island Utils"));
    private static final Component desc = Component.literal(translate("&6Music Resources"));
    HttpClient client;
    Gson gson;

    public PackDownloadListener currentDownload = null;
    public boolean getting = false;
    public boolean accepted = false;
    public static Pack pack;

    private static String getResourcepackUrl() {
        return IslandUtils.isPreRelease() ? url_pre : url_release;
    }

    public ResourcePackUpdater() {
        client = HttpClient.newBuilder().build();
        gson = new Gson();
    }

    public CompletableFuture<Void> downloadAndApply() {
        Minecraft minecraft = Minecraft.getInstance();
        logger.info("Downloading resource pack...");

        return CompletableFuture.runAsync(() -> {
            this.currentDownload = new PackDownloadListener();

            Path outputFile = ResourcePackOptions.packZip;

            try {
                FileUtil.createDirectoriesSafe(outputFile.getParent());

                URL url = new URL(ResourcePackOptions.data.url);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection(minecraft.getProxy());
                urlConnection.setInstanceFollowRedirects(true);
                InputStream inputStream = urlConnection.getInputStream();

                try (OutputStream outputStream = Files.newOutputStream(outputFile, StandardOpenOption.CREATE)){
                    int j;
                    byte[] bs = new byte[8196];
                    long l = 0L;
                    while ((j = inputStream.read(bs)) >= 0) {
                        this.currentDownload.downloadedBytes(l);
                        outputStream.write(bs, 0, j);
                    }
                }

                logger.info("Applying resource pack...");
                apply(outputFile.toFile(), true);
            } catch (Exception e) {
                this.currentDownload = null;
                logger.error("Failed to download resource pack. ", e);
            }
        });
    }

    public void apply(File file, Boolean save) {
        getting = false;
        currentDownload = null;
        pack = new Pack(
                new PackLocationInfo("island_utils", title, PackSource.BUILT_IN, Optional.empty()),
                new FilePackResources.FileResourcesSupplier(file),
                new Pack.Metadata(desc, PackCompatibility.COMPATIBLE, FeatureFlagSet.of(), List.of()),
                new PackSelectionConfig(true, Pack.Position.BOTTOM, true)
        );
        if (save) {
            try { ResourcePackOptions.save(); }
            catch (IOException e) { System.err.println("Failed to save resource pack options"); }
        }
    }

    public CompletableFuture<Void> get() {
        return CompletableFuture.runAsync(this::doGet);
    }

    private void doGet() {
        File file = ResourcePackOptions.packZip.toFile();
        if (file.exists()) apply(file, false);

        logger.info("Requesting resource pack...");
        try {
            ResourcePack current = ResourcePackOptions.get();
            logger.info("Current resource pack version: " + current);

            ResourcePack rp = requestUpdate();
            logger.info("Received Resource Pack: " + rp.rev);
            if (current != null && Objects.equals(current.rev, rp.rev) && file.exists()) {
                logger.info("Resource pack has not changed. Not downloading!");
                apply(file, false);
                return;
            }
            ResourcePackOptions.data = rp;

            CompletableFuture<Void> download = downloadAndApply();
            download.thenAccept((v) -> {
                getting = false;
            });
        } catch (Exception e) {
            getting = false;
            logger.error("Failed to get IslandUtils resource pack info!", e);
        }
    }

    private ResourcePack requestUpdate() throws Exception {
        HttpRequest req = HttpRequest.newBuilder(URI.create(getResourcepackUrl())).GET().build();
        logger.info("Requesting resource pack: " + req.uri());
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (res.statusCode() != 200) {
            throw new RuntimeException("Got " + res.statusCode() + "code from github. Response:" + res.body());
        }
        return gson.fromJson(res.body(), ResourcePack.class);
    }

    public static class PackDownloadListener implements HttpUtil.DownloadProgressListener {
        private OptionalLong size = OptionalLong.empty();
        private long bytesDownloaded = 0;

        @Override
        public void downloadStart(OptionalLong optionalLong) {
            logger.info("Downloading IslandUtils Resources... Size: " + optionalLong);
            size = optionalLong;
        }

        @Override
        public void downloadedBytes(long l) {
            bytesDownloaded = l;
        }

        public OptionalLong getSize() {
            return size;
        }
        public long getBytesDownloaded() {
            return bytesDownloaded;
        }

        // don't care crown
        @Override
        public void requestFinished(boolean bl) {
        }
        @Override
        public void requestStart() {
        }
    }

}

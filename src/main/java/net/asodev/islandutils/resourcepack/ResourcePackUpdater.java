package net.asodev.islandutils.resourcepack;

import com.google.gson.Gson;
import net.asodev.islandutils.resourcepack.schema.ResourcePack;
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
    private static final String url = "http://static.islandutils.asodev.net/pack.json";
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
        System.out.println("Downloading resource pack...");

        state = new ProgressScreen(false);

        File file = new File(ResourcePackOptions.packZip);
        CompletableFuture<?> future = HttpUtil.downloadTo(file, new URL(ResourcePackOptions.data.url), new HashMap<>(), 0xFA00000, state, Minecraft.getInstance().getProxy());
        future.thenAccept(obj -> {
            System.out.println("Applying resource pack...");
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
        File file = new File(ResourcePackOptions.packZip);
        if (file.exists()) apply(file, false);

        System.out.println("Requesting resource pack...");
        try {
            ResourcePack current = ResourcePackOptions.get();
            requestUpdate().thenAccept(rp -> {
                System.out.println("Received Resource Pack: " + rp.hash);
                if (current != null && Objects.equals(current.hash, rp.hash) && file.exists()) {
                    System.out.println("Resource pack has not changed. Not downloading!");
                    apply(file, false);
                    return;
                }
                ResourcePackOptions.data = rp;
                try {
                    getting = true;
                    downloadAndApply();
                } catch (Exception e) {
                    getting = false;
                    System.err.println("Failed to download resource pack!");
                }
            });
        } catch (Exception e) {
            getting = false;
            System.err.println("Failed to get IslandUtils resource pack info! " + e.getMessage());
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

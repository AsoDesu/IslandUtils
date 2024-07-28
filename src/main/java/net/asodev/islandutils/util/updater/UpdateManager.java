package net.asodev.islandutils.util.updater;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.terraformersmc.modmenu.util.mod.Mod;
import net.asodev.islandutils.IslandUtils;
import net.asodev.islandutils.util.updater.schema.AvailableUpdate;
import net.asodev.islandutils.util.updater.schema.ModrinthVersion;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.minecraft.SharedConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class UpdateManager {
    private static final Logger logger = LoggerFactory.getLogger(UpdateManager.class);
    HttpClient client;
    Gson gson;

    public UpdateManager() {
        client = HttpClient.newBuilder().build();
        gson = new Gson();
    }

    public CompletableFuture<List<ModrinthVersion>> checkForUpdates() throws Exception {
        CompletableFuture<List<ModrinthVersion>> f = new CompletableFuture<>();
        String version = "[\"" + SharedConstants.getCurrentVersion().getName() + "\"]";
        String url = String.format(
                "https://api.modrinth.com/v2/project/island-utils/version?game_versions=%s&loaders=%s",
                URLEncoder.encode(version, StandardCharsets.UTF_8),
                URLEncoder.encode("[\"fabric\"]", StandardCharsets.UTF_8)
        );

        URI updatorURI = new URI(url);
        HttpRequest req = HttpRequest.newBuilder(updatorURI).GET().build();

        client.sendAsync(req, HttpResponse.BodyHandlers.ofString()).thenAccept(res -> {
            TypeToken<?> type = TypeToken.getParameterized(List.class, ModrinthVersion.class);
            List<ModrinthVersion> json = (List<ModrinthVersion>) gson.fromJson(res.body(), type);
            f.complete(json);
        });

        return f;
    }

    public void runUpdateCheck() {
        try {
            checkForUpdates().thenAccept(res -> {
                if (res == null) return;
                ModrinthVersion newVersion = null;
                for (ModrinthVersion version : res) {
                    try {
                        Version updateVersion = Version.parse(version.version_number());
                        if (IslandUtils.version.compareTo(updateVersion) < 0) {
                            newVersion = version;
                            break;
                        }
                    } catch (VersionParsingException e) {
                        logger.error("Unable to parse version: '" + version.version_number() + "'");
                    }
                }
                if (newVersion != null) {
                    String version = newVersion.version_number();
                    IslandUtils.availableUpdate =
                            new AvailableUpdate(newVersion.name(), version, "https://modrinth.com/mod/island-utils/version/" + version);
                }
            });
        } catch (Exception e) {
            logger.error("Failed to get IslandUtils Update!");
        }
    }

}

package net.asodev.islandutils.updator;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.asodev.islandutils.updator.schema.GithubRelease;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Updater {

    HttpClient client;
    Gson gson;

    boolean updateAvalible = false;
    public Updater() {
        client = HttpClient.newBuilder().build();
        gson = new Gson();
    }

    public CompletableFuture<List<GithubRelease>> checkForUpdates() throws Exception {
        CompletableFuture<List<GithubRelease>> f = new CompletableFuture<>();

        URI updatorURI = new URI("https://api.github.com/repos/AsoDesu/IslandUtils/releases");
        HttpRequest req = HttpRequest.newBuilder(updatorURI).GET().build();

        Type listType = new TypeToken<ArrayList<GithubRelease>>(){}.getType();
        client.sendAsync(req, HttpResponse.BodyHandlers.ofString()).thenAccept(res -> {
            f.complete(gson.fromJson(res.body(), listType));
        });

        return f;
    }

}

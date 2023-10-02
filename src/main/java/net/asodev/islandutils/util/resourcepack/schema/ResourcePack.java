package net.asodev.islandutils.util.resourcepack.schema;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.asodev.islandutils.util.resourcepack.ResourcePackUpdater;

public class ResourcePack {

    @SerializedName("url")
    @Expose
    public String url;

    @SerializedName("rev")
    @Expose
    public Integer rev;

    public String toJson() {
        return (new Gson()).toJson(this);
    }

    public static ResourcePack fromJson(String res) {
        JsonObject object = new Gson().fromJson(res, JsonObject.class);
        if (object.has("hash")) {
            ResourcePackUpdater.logger.info("Ignoring current pack file (Using the old format)");
            return null;
        }

        return (new Gson()).fromJson(object, ResourcePack.class);
    }

    @Override
    public String toString() {
        return "ResourcePack{" +
                "url='" + url + '\'' +
                ", rev=" + rev +
                '}';
    }
}

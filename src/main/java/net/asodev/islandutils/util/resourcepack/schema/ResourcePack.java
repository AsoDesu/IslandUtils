package net.asodev.islandutils.util.resourcepack.schema;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResourcePack {

    @SerializedName("url")
    @Expose
    public String url;

    @SerializedName("hash")
    @Expose
    public String hash;

    public String toJson() {
        return (new Gson()).toJson(this);
    }

    public static ResourcePack fromJson(String res) {
        return (new Gson()).fromJson(res, ResourcePack.class);
    }

}

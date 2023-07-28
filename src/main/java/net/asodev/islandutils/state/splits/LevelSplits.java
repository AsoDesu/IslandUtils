package net.asodev.islandutils.state.splits;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.asodev.islandutils.util.ChatUtils;

import java.util.HashMap;
import java.util.Map;

public class LevelSplits {

    private String name;
    private Long expires;
    private Map<String, Long> splits = new HashMap<>();
    private Map<String, String> levelNames = new HashMap<>();

    public LevelSplits(String name) {
        this.name = name;
    }
    public LevelSplits(JsonObject json) {
        name = json.get("name").getAsString();
        expires = json.get("expires").getAsLong();

        Map<String, JsonElement> splitMap = json.getAsJsonObject("splits").asMap();
        splitMap.forEach((hash, element) -> splits.put(hash, element.getAsLong()));

        Map<String, JsonElement> splitNames = json.getAsJsonObject("names").asMap();
        splitNames.forEach((hash, element) -> levelNames.put(hash, element.getAsString()));
    }

    public void saveSplit(String uid, String name, Long time) {
        Long currentTime = splits.get(uid);
        if (currentTime != null && currentTime < time) {
            return;
        }
        splits.put(uid, time);
        levelNames.put(uid, name);
        ChatUtils.debug("LevelSplits - Time (" + time + "ms) was saved with uid: " + uid);
        SplitManager.saveAsync();
    }
    public Double getSplit(String level) {
        return splits.containsKey(level) ? splits.get(level) / 1000d : null;
    }

    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("name", name);
        object.addProperty("expires", expires);

        JsonObject splitObject = new JsonObject();
        this.splits.forEach(splitObject::addProperty);
        object.add("splits", splitObject);

        JsonObject levelNames = new JsonObject();
        this.splits.forEach(levelNames::addProperty);
        object.add("names", levelNames);

        return object;
    }

    public String getName() {
        return name;
    }
    public Long getExpires() {
        return expires;
    }
    public void setExpires(Long expires) {
        this.expires = expires;
    }
}

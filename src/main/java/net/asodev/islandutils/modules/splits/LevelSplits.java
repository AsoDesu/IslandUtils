package net.asodev.islandutils.modules.splits;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.util.ChatUtils;

import java.util.*;

public class LevelSplits {

    private String name;
    private Long expires = null;
    private Map<String, Split> splits = new HashMap<>();
    private Map<String, String> levelNames = new HashMap<>();

    public LevelSplits(String name) {
        this.name = name;
    }
    public LevelSplits(JsonObject json) {
        name = json.get("name").getAsString();
        JsonElement expiresElement = json.get("expires");
        if (!expiresElement.isJsonNull())
            expires = expiresElement.getAsLong();

        Map<String, JsonElement> splitMap = json.getAsJsonObject("splits").asMap();
        splitMap.forEach((hash, element) -> splits.put(hash, Split.fromJson(element)));

        Map<String, JsonElement> splitNames = json.getAsJsonObject("names").asMap();
        splitNames.forEach((hash, element) -> levelNames.put(hash, element.getAsString()));
    }

    public void saveSplit(String uid, String name, Long time) {
        Split currentSplit = splits.get(uid);
        if (currentSplit == null) {
            currentSplit = new Split(time, time.doubleValue(), List.of(time));
        } else {
            currentSplit = currentSplit.addTime(time);
        }

        splits.put(uid, currentSplit);
        levelNames.put(uid, name);
        ChatUtils.debug("LevelSplits - Time (" + time + "ms) was saved with uid: " + uid);
        SplitManager.saveAsync();
    }
    public Double getSplit(String level) {
        if (!splits.containsKey(level)) return null;
        SplitType type = IslandOptions.getSplits().getSaveMode();
        double value = 0.0;
        switch (type) {
            case BEST -> value = splits.get(level).best();
            case AVG -> value =  splits.get(level).avg();
        }
        return value / 1000d;
    }

    public Optional<Split> getRawSplit(String level) {
        return Optional.ofNullable(splits.get(level));
    }

    public Optional<Split> getSplitNoColor(String level) {
        List<String> allPossibleLevels = List.of(
                String.format("green%s", level),
                String.format("yellow%s", level),
                String.format("red%s", level)
        );
        Optional<String> match = allPossibleLevels.stream().filter(s -> splits.containsKey(s)).findFirst();
        return match.map(s -> splits.get(s));
    }

    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("name", name);
        object.addProperty("expires", expires);

        JsonObject splitObject = new JsonObject();
        this.splits.forEach((hash, split) -> splitObject.add(hash, split.toJson()));
        object.add("splits", splitObject);

        JsonObject levelNames = new JsonObject();
        this.levelNames.forEach(levelNames::addProperty);
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

    public record Split(Long best, Double avg, List<Long> times){
        public Split addTime(Long time) {
            List<Long> newList = new ArrayList<>(times);
            newList.add(time);
            double newAvg = newList.stream().mapToDouble(d -> d).average().orElse(0.0);
            Long newBest = time < best ? time : best;
            return new Split(newBest, newAvg, Collections.unmodifiableList(newList));
        }

        public static Split fromJson(JsonElement json) {
            return new Gson().fromJson(json, Split.class);
        }
        public JsonElement toJson() {
            return new Gson().toJsonTree(this);
        }
    }
}

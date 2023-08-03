package net.asodev.islandutils.state.splits;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.asodev.islandutils.IslandUtilsEvents;
import net.asodev.islandutils.state.GAME;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.util.ChatUtils;
import net.asodev.islandutils.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static net.asodev.islandutils.util.resourcepack.ResourcePackOptions.islandFolder;

public class SplitManager {
    private static Logger logger = LoggerFactory.getLogger(SplitManager.class);
    private static final File file = new File(islandFolder + "/splits.json");

    private static final Map<String, LevelSplits> courseSplits = new HashMap<>();
    private static Long currentCourseExpiry = null;

    public static void init() {
        SplitManager.load();

        IslandUtilsEvents.GAME_CHANGE.register((game) -> {
            if (game != GAME.PARKOUR_WARRIOR_DOJO) LevelTimer.setInstance(null);
        });
    }

    public static LevelSplits getCourseSplits(String courseName) {
        String name = courseName.toLowerCase().contains("daily challenge") ? "daily" : courseName;
        LevelSplits levelSplits = courseSplits.get(name);

        if (levelSplits == null || System.currentTimeMillis() >= levelSplits.getExpires()) {
            levelSplits = new LevelSplits(name);
            levelSplits.setExpires(currentCourseExpiry);
            courseSplits.put(name, levelSplits);
            ChatUtils.debug("LevelTimer - Created splits for: " + name);
        } else {
            ChatUtils.debug("SplitManager - Found splits for: " + name);
        }
        return levelSplits;
    }

    public static void saveAsync() {
        Utils.savingQueue.submit(() -> {
            try {
                save();
                logger.info("Saved splits!");
            } catch (Exception e) {
                logger.error("Failed to save splits", e);
            }
        });
    }

    public static void save() throws IOException {
        JsonObject object = new JsonObject();
        JsonArray array = new JsonArray();
        for (Map.Entry<String, LevelSplits> split : courseSplits.entrySet()) {
            array.add(split.getValue().toJson());
        }

        object.add("splits", array);
        object.addProperty("savedAt", System.currentTimeMillis());
        object.addProperty("version", 1);
        Utils.writeFile(file, object.toString());
    }
    private static void load() {
        try {
            String string = Utils.readFile(file);
            if (string == null) return;

            JsonObject object = new Gson().fromJson(string, JsonObject.class);
            for (JsonElement element : object.getAsJsonArray("splits").asList()) {
                LevelSplits splits = new LevelSplits(element.getAsJsonObject());
                courseSplits.put(splits.getName(), splits);
            }
        } catch (Exception e) {
            logger.error("Failed to load splits", e);
        }
    }

    public static Long getCurrentCourseExpiry() {
        return currentCourseExpiry;
    }

    public static void setCurrentCourseExpiry(Long currentCourseExpiry) {
        SplitManager.currentCourseExpiry = currentCourseExpiry;
    }
}

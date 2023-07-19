package net.asodev.islandutils.state.crafting.state;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.asodev.islandutils.state.crafting.CraftingMenuType;
import net.asodev.islandutils.util.Scheduler;
import net.asodev.islandutils.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static net.asodev.islandutils.resourcepack.ResourcePackOptions.islandFolder;

public class CraftingItems {
    private static Logger logger = LoggerFactory.getLogger(CraftingItems.class);
    private static final File file = new File(islandFolder + "/crafting.json");
    private static final List<CraftingItem> items = new ArrayList<>();

    private static boolean saveQueued = false;
    private static final ExecutorService craftingQueue = Executors.newFixedThreadPool(2);

    /**
     * Adds an item into the items
     * @param item The CraftingItem to add
     */
    public static void addItem(CraftingItem item) {
        removeSlot(item.getCraftingMenuType(), item.getSlot());
        items.add(item);

        save();
    }

    /**
     * Remove items in the same menu & slot
     *
     * @param type The menu type (Forge or Assembler)
     * @param slot The slot you wish to remove
     */
    public static void removeSlot(CraftingMenuType type, int slot) {
        items.removeIf(i -> i.getCraftingMenuType() == type && i.getSlot() == slot);
        save();
    }

    public static <T> void submit(Runnable task) {
        craftingQueue.submit(task);
    }
    public static List<CraftingItem> getItems() {
        return items;
    }

    public static void load() throws Exception {
        String string = Utils.readFile(file);
        if (string == null) return;

        JsonObject object = new Gson().fromJson(string, JsonObject.class);
        JsonArray array = object.get("items").getAsJsonArray();
        array.forEach(element -> items.add(CraftingItem.fromJson(element)));
    }
    public static void save() {
        if (saveQueued) {
            logger.info("Deferred save because one is already queued!"); // TODO: Remove this log
            return;
        }

        saveQueued = true;
        Scheduler.schedule(3, (client) -> {
            craftingQueue.submit(CraftingItems::saveSync);
            saveQueued = false;
        });
    }
    public static void saveSync() {
        try {
            Utils.assertIslandFolder();

            JsonArray array = new JsonArray();
            items.forEach(item -> {
                // TODO: Make this safer - toJson could throw an error
                array.add(item.toJson());
            });

            JsonObject object = new JsonObject();
            object.add("items", array);
            object.addProperty("savedAt", System.currentTimeMillis());

            Utils.writeFile(file, object.toString());
            logger.info("Saved Crafting Items");
        } catch (Exception e) {
            logger.error("Failed to save crafting items", e);
        }
    }

}

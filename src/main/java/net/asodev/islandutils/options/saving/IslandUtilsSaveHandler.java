package net.asodev.islandutils.options.saving;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.asodev.islandutils.modules.music.MusicManager;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.options.categories.MusicOptions;
import net.asodev.islandutils.options.categories.OptionsCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class IslandUtilsSaveHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(IslandOptions.class);
    private final Gson gson = new Gson();

    public void save(OptionsCategory category, JsonObject object) throws IllegalAccessException {
        for (Field declaredField : category.getClass().getDeclaredFields()) {
            if (declaredField.isAnnotationPresent(Ignore.class)) continue;
            declaredField.trySetAccessible();

            String name = declaredField.getName();
            Object field = declaredField.get(category);
            object.add(name, gson.toJsonTree(field));
        }
    }

    public void load(OptionsCategory category, JsonObject object) {
        if (category instanceof MusicOptions musicOptions) {
            var modifiers = object.getAsJsonObject("modifiers");
            MusicManager.getModifiers().forEach((modifier) -> {
                if (!modifiers.has(modifier.identifier())) {
                    musicOptions.setModifierEnabled(modifier, modifier.defaultOption());
                } else {
                    var value = modifiers.get(modifier.identifier()).getAsBoolean();
                    musicOptions.setModifierEnabled(modifier, value);
                }
            });
        } else {
            for (Field field : category.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Ignore.class)) continue;
                String name = field.getName();
                JsonElement jsonElement = object.get(name);
                if (jsonElement == null) continue;

                field.trySetAccessible();
                try {
                    Class<?> type = field.getType();
                    field.set(category, gson.fromJson(jsonElement, type));
                } catch (Exception e) {
                    LOGGER.warn("Failed to load config option: " + name, e);
                }
            }
        }
    }
}

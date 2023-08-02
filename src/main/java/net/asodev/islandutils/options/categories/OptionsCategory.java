package net.asodev.islandutils.options.categories;

import com.google.gson.JsonObject;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Field;

public interface OptionsCategory {
    ConfigCategory getCategory();
}

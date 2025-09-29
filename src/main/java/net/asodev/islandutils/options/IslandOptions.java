package net.asodev.islandutils.options;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import net.asodev.islandutils.discord.DiscordPresenceUpdator;
import net.asodev.islandutils.modules.splits.LevelTimer;
import net.asodev.islandutils.options.categories.ClassicOptions;
import net.asodev.islandutils.options.categories.CosmeticsOptions;
import net.asodev.islandutils.options.categories.CraftingOptions;
import net.asodev.islandutils.options.categories.DiscordOptions;
import net.asodev.islandutils.options.categories.MiscOptions;
import net.asodev.islandutils.options.categories.MusicOptions;
import net.asodev.islandutils.options.categories.OptionsCategory;
import net.asodev.islandutils.options.categories.PlobbyOptions;
import net.asodev.islandutils.options.categories.SplitsCategory;
import net.asodev.islandutils.options.saving.IslandUtilsSaveHandler;
import net.asodev.islandutils.util.Utils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IslandOptions {
    private static final Logger LOGGER = LoggerFactory.getLogger(IslandOptions.class);
    private static final File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "islandutils.json");
    private static final IslandUtilsSaveHandler saveHandler = new IslandUtilsSaveHandler();
    private static final List<OptionsCategory> categories = new ArrayList<>();
    private static final MusicOptions music = new MusicOptions();
    private static final CosmeticsOptions cosmetics = new CosmeticsOptions();
    private static final DiscordOptions discord = new DiscordOptions();
    private static final ClassicOptions classicHITW = new ClassicOptions();
    private static final CraftingOptions crafting = new CraftingOptions();
    private static final SplitsCategory splits = new SplitsCategory();
    private static final PlobbyOptions plobby = new PlobbyOptions();
    private static final MiscOptions misc = new MiscOptions();

    public static void init() {
        categories.add(music);
        categories.add(cosmetics);
        categories.add(discord);
        categories.add(classicHITW);
        categories.add(crafting);
        categories.add(splits);
        categories.add(plobby);
        categories.add(misc);
        load();
    }

    private static void load() {
        if (!configFile.exists()) { return; }
        JsonObject object;
        try {
            String s = Utils.readFile(configFile);
            object = (new Gson()).fromJson(s, JsonObject.class);
        } catch (Exception e) {
            LOGGER.error("Failed to load IslandUtils config file", e);
            return;
        }

        for (OptionsCategory category : categories) {
            saveHandler.load(category, object);
        }
    }

    public static void save() {
        DiscordPresenceUpdator.updateFromConfig(discord);
        LevelTimer.updateFromConfig(splits);

        JsonObject object = new JsonObject();
        for (OptionsCategory category : categories) {
            try {
                saveHandler.save(category, object);
            } catch (IllegalAccessException e) {
                LOGGER.error("Failed to save category: " + category, e);
                return;
            }
        }

        try {
            Utils.writeFile(configFile, object.toString());
        } catch (IOException e) {
            LOGGER.error("Failed to save IslandUtils options!", e);
        }

        LOGGER.info("Saved IslandUtils Options!");
    }
    public static MusicOptions getMusic() {
        return music;
    }
    public static CosmeticsOptions getCosmetics() {
        return cosmetics;
    }
    public static DiscordOptions getDiscord() {
        return discord;
    }
    public static ClassicOptions getClassicHITW() {
        return classicHITW;
    }
    public static CraftingOptions getCrafting() {
        return crafting;
    }
    public static SplitsCategory getSplits() {
        return splits;
    }
    public static PlobbyOptions getPlobby() {
        return plobby;
    }
    public static MiscOptions getMisc() {
        return misc;
    }


    public static Screen getScreen(Screen parent) {
        List<ConfigCategory> yaclCategories = categories.stream().map(OptionsCategory::getCategory).toList();

        return YetAnotherConfigLib.createBuilder()
                .title(Component.translatable("text.autoconfig.islandutils.title"))
                .save(IslandOptions::save)
                .categories(yaclCategories)
                .build()
                .generateScreen(parent);
    }

}

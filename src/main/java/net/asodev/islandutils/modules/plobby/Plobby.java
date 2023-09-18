package net.asodev.islandutils.modules.plobby;

import net.asodev.islandutils.modules.plobby.state.PlobbyStateProvider;
import net.asodev.islandutils.util.ChatUtils;
import net.asodev.islandutils.util.Utils;
import net.asodev.islandutils.util.resourcepack.ResourcePackOptions;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;

import static net.asodev.islandutils.modules.crafting.CraftingUI.CHEST_BACKGROUND_STYLE;
import static net.asodev.islandutils.util.resourcepack.ResourcePackOptions.islandFolder;

public class Plobby {
    private static final Logger LOGGER = LoggerFactory.getLogger(Plobby.class);
    public static final Path plobbyFolder = islandFolder.resolve("plobby");
    private static final File plobbyCodeFile = plobbyFolder.resolve("code.txt").toFile();

    PlobbyStateProvider stateProvider;
    PlobbyUI ui;
    public Plobby(PlobbyStateProvider stateProvider) {
        this.stateProvider = stateProvider;
        this.ui = new PlobbyUI(stateProvider);

        stateProvider.setCodeUpdateCallback(Plobby::saveCodeToFile);
        stateProvider.setLockStateCallback(this::onLockStateChange);
    }

    public static void saveCodeToFile(String code) {
        Utils.assertIslandFolder();

        File folder = plobbyFolder.toFile();
        if (!folder.exists()) folder.mkdir();

        Utils.savingQueue.submit(() -> {
            try {
                Utils.writeFile(plobbyCodeFile, code);
            } catch (IOException e) {
                LOGGER.error("Failed to save plobby code file: " + e.getMessage());
            }
        });
    }

    public void onLockStateChange(Boolean state) {
    }
    public void onDisband() {
        saveCodeToFile("");
    }

    public PlobbyUI getUi() {
        return ui;
    }

    private static Plobby instance = null;
    public static void create(PlobbyStateProvider state) {
        instance = new Plobby(state);
    }
    public static void disband() {
        if (instance == null) return;
        instance.onDisband();
        instance = null;
    }

    public static Plobby getInstance() {
        return instance;
    }
    public static boolean hasInstance() {
        return instance != null;
    }

    private static Component titleComponent;
    private static Component createComponent;
    public static Component getTitleComponent() {
        return titleComponent;
    }
    public static Component getCreateComponent() {
        return createComponent;
    }
    public static void setTitleCharacter(String titleCharacter) {
        titleComponent = Component.literal(titleCharacter).withStyle(CHEST_BACKGROUND_STYLE);
    }
    public static void setCreateCharacter(String titleCharacter) {
        createComponent = Component.literal(titleCharacter).withStyle(CHEST_BACKGROUND_STYLE);
    }
}

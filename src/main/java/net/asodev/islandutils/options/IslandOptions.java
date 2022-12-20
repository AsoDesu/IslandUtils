package net.asodev.islandutils.options;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.client.gui.screens.Screen;

@Config(name = "islandutils")
public class IslandOptions implements ConfigData {

    @ConfigEntry.Category("music")
    boolean tgttosMusic = true;
    @ConfigEntry.Category("music")
    boolean hitwMusic = true;
    @ConfigEntry.Category("music")
    boolean bbMusic = true;
    @ConfigEntry.Category("music")
    boolean sbMusic = true;
    @ConfigEntry.Category("music")
    boolean tgttosDoubleTime = true;

    @ConfigEntry.Category("cosmetics")
    boolean showPlayerPreview = true;
    @ConfigEntry.Category("cosmetics")
    boolean showOnHover = true;
    @ConfigEntry.Category("cosmetics")
    boolean showOnOnlyCosmeticMenus = true;

    @ConfigEntry.Category("discord")
    public boolean discordPresence = true;
    @ConfigEntry.Category("discord")
    @ConfigEntry.Gui.Tooltip()
    public boolean showGame = true;
    @ConfigEntry.Category("discord")
    @ConfigEntry.Gui.Tooltip()
    public boolean showGameInfo = true;
    @ConfigEntry.Category("discord")
    @ConfigEntry.Gui.Tooltip()
    public boolean showTimeRemaining = true;
    @ConfigEntry.Category("discord")
    @ConfigEntry.Gui.Tooltip()
    public boolean showTimeElapsed = true;
    @ConfigEntry.Category("discord")
    @ConfigEntry.Gui.Tooltip()
    public boolean showFactionLevel = true;

    @ConfigEntry.Category("misc")
    boolean pauseConfirm = true;

    @ConfigEntry.Category("misc")
    @ConfigEntry.Gui.Tooltip()
    boolean hideSliders = false;

    @ConfigEntry.Category("misc")
    boolean debugMode = false;

    public boolean isTgttosMusic() {
        return tgttosMusic;
    }
    public boolean isHitwMusic() {
        return hitwMusic;
    }
    public boolean isBbMusic() {
        return bbMusic;
    }
    public boolean isSbMusic() {
        return sbMusic;
    }
    public boolean isTgttosDoubleTime() {
        return tgttosDoubleTime;
    }
    public boolean isShowPlayerPreview() {
        return showPlayerPreview;
    }
    public boolean isPauseConfirm() {
        return pauseConfirm;
    }
    public boolean isHideSliders() {
        return hideSliders;
    }

    public boolean isShowOnHover() {
        return showOnHover;
    }
    public boolean isShowOnOnlyCosmeticMenus() {
        return showOnOnlyCosmeticMenus;
    }
    public boolean isDebugMode() {
        return debugMode;
    }

    public static Screen getScreen(Screen parent) {
        return AutoConfig.getConfigScreen(IslandOptions.class, parent).get();
    }
    public static IslandOptions getOptions() {
        return AutoConfig.getConfigHolder(IslandOptions.class).getConfig();
    }

}

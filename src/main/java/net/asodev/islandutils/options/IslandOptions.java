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

    @ConfigEntry.Category("cosmetics")
    boolean showPlayerPreview = true;
    @ConfigEntry.Category("cosmetics")
    boolean showOnHover = true;
    @ConfigEntry.Category("cosmetics")
    boolean showOnOnlyCosmeticMenus = true;

    @ConfigEntry.Category("misc")
    boolean pauseConfirm = true;

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
    public boolean isShowPlayerPreview() {
        return showPlayerPreview;
    }
    public boolean isPauseConfirm() {
        return pauseConfirm;
    }
    public boolean isShowOnHover() {
        return showOnHover;
    }
    public boolean isShowOnOnlyCosmeticMenus() {
        return showOnOnlyCosmeticMenus;
    }

    public static Screen getScreen(Screen parent) {
        return AutoConfig.getConfigScreen(IslandOptions.class, parent).get();
    }
    public static IslandOptions getOptions() {
        return AutoConfig.getConfigHolder(IslandOptions.class).getConfig();
    }

}

package net.asodev.islandutils.options;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

@Config(name = "islandutils")
public class IslandOptions implements ConfigData {

    boolean tgttosMusic = true;
    boolean hitwMusic = true;
    boolean bbMusic = true;
    boolean sbMusic = true;

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

    public static Screen getScreen(Screen parent) {
        return AutoConfig.getConfigScreen(IslandOptions.class, parent).get();
    }
    public static IslandOptions getOptions() {
        return AutoConfig.getConfigHolder(IslandOptions.class).getConfig();
    }

}

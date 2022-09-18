package net.asodev.islandutils.options;

import dev.stashy.soundcategories.CategoryLoader;
import net.minecraft.sounds.SoundSource;

public class IslandSoundCategories implements CategoryLoader {
    @Register(id = "MCC_MUSIC", master = true)
    public static SoundSource MUSIC;
}

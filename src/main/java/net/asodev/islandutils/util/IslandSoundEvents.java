package net.asodev.islandutils.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class IslandSoundEvents {
    public static final SoundEvent UI_CLICK_NORMAL = soundEvent("ui.click_normal");
    public static final SoundEvent UI_ACHIEVEMENT_RECEIVE = soundEvent("ui.achievement_receive");
    public static final SoundEvent ANNOUNCER_GAME_OVER = islandUtilsEvent("announcer.gameover");

    private static SoundEvent soundEvent(String path) {
        return SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("mcc", path));
    }

    private static SoundEvent islandUtilsEvent(String path) {
        return SoundEvent.createVariableRangeEvent(islandSound(path));
    }

    public static ResourceLocation islandSound(String path) {
        return ResourceLocation.fromNamespaceAndPath("island", path);
    }
}

package net.asodev.islandutils.util;

import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class IslandSoundEvents {
    public static final SoundEvent UI_CLICK_NORMAL = soundEvent("ui.click_normal");
    public static final SoundEvent UI_ACHIEVEMENT_RECEIVE = soundEvent("ui.achievement_receive");
    public static final SoundEvent ANNOUNCER_GAME_OVER = islandUtilsEvent("announcer.gameover");

    private static SoundEvent soundEvent(String path) {
        return SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("mcc", path));
    }

    private static SoundEvent islandUtilsEvent(String path) {
        return SoundEvent.createVariableRangeEvent(islandSound(path));
    }

    public static Identifier islandSound(String path) {
        return Identifier.fromNamespaceAndPath("island", path);
    }
}

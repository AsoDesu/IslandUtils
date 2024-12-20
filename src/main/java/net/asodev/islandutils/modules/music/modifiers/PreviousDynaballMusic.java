package net.asodev.islandutils.modules.music.modifiers;

import net.asodev.islandutils.modules.music.SoundInfo;
import net.asodev.islandutils.modules.music.TrackMusicModifier;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.util.IslandSoundEvents;
import net.minecraft.resources.ResourceLocation;

public class PreviousDynaballMusic extends TrackMusicModifier {
    public PreviousDynaballMusic(){
        super("music.global.dynaball", "dynaball.old_music");
    }

    @Override
    public boolean defaultOption() {
        return false;
    }

    @Override
    public SoundInfo apply(SoundInfo info) {
        return info.withPath(IslandSoundEvents.islandSound("island.music.dynaball"));
    }
}

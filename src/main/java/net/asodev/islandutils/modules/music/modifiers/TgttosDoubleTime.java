package net.asodev.islandutils.modules.music.modifiers;

import net.asodev.islandutils.modules.music.SoundInfo;
import net.asodev.islandutils.modules.music.TrackMusicModifier;
import net.asodev.islandutils.state.MccIslandState;
import net.minecraft.resources.Identifier;

public class TgttosDoubleTime extends TrackMusicModifier {
    public TgttosDoubleTime(){
        super("music.global.tgttosawaf", "tgttos.double_time");
    }

    @Override
    public SoundInfo apply(SoundInfo info) {
        return info.withPitch(1.2f);
    }

    @Override
    public boolean shouldApply1(Identifier soundLocation) {
        return MccIslandState.getModifier().equals("DOUBLE TIME");
    }
}

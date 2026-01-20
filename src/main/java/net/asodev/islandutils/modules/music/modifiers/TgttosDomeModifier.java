package net.asodev.islandutils.modules.music.modifiers;

import net.asodev.islandutils.modules.music.SoundInfo;
import net.asodev.islandutils.modules.music.TrackMusicModifier;
import net.asodev.islandutils.state.MccIslandState;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class TgttosDomeModifier extends TrackMusicModifier {
    public TgttosDomeModifier(){
        super("music.global.tgttosawaf", "tgttos.dome_modifier");
    }

    @Override
    public SoundInfo apply(SoundInfo info) {
        return info.withPath(Identifier.fromNamespaceAndPath("island", "island.music.to_the_dome"));
    }

    @Override
    public boolean shouldApply1(Identifier soundLocation) {
        return MccIslandState.getModifier().equals("TO THE DOME");
    }
}

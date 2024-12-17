package net.asodev.islandutils.modules.music.modifiers;

import net.asodev.islandutils.modules.music.MusicModifier;
import net.asodev.islandutils.modules.music.SoundInfo;
import net.asodev.islandutils.modules.music.TrackMusicModifier;
import net.asodev.islandutils.state.Game;
import net.asodev.islandutils.util.IslandSoundEvents;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class HighQualityMusic extends MusicModifier {
    Map<String, ResourceLocation> replacements = Map.of(
            "music.global.parkour_warrior", Game.PARKOUR_WARRIOR_DOJO.getMusicLocation(),
            "music.global.battle_box", Game.BATTLE_BOX.getMusicLocation(),
            "music.global.dynaball", Game.DYNABALL.getMusicLocation(),
            "music.global.hole_in_the_wall", Game.HITW.getMusicLocation(),
            "music.global.rocket_spleef", Game.ROCKET_SPLEEF_RUSH.getMusicLocation(),
            "music.global.sky_battle", Game.SKY_BATTLE.getMusicLocation(),
            "music.global.tgttosawaf", Game.TGTTOS.getMusicLocation()
    );

    public HighQualityMusic(){
        super("global.hq");
    }

    @Override
    public SoundInfo apply(SoundInfo info) {
        ResourceLocation replacementPath = replacements.get(info.path().getPath());
        return replacementPath != null ? info.withPath(replacementPath) : info;
    }

    @Override
    public boolean defaultOption() {
        return false;
    }

    @Override
    public boolean shouldApply(ResourceLocation soundLocation) {
        return true;
    }
}

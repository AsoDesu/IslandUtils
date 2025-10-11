package net.asodev.islandutils.modules.music.modifiers;

import net.asodev.islandutils.modules.music.HubMusic;
import net.asodev.islandutils.modules.music.MusicModifier;
import net.asodev.islandutils.modules.music.SoundInfo;
import net.asodev.islandutils.state.Game;
import net.asodev.islandutils.util.ChatUtils;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class HighQualityMusic extends MusicModifier {
    Map<String, ResourceLocation> replacements = new HashMap<>();

    public HighQualityMusic() {
        super("global.hq");
        replacements.putAll(Map.of(
                "music.global.hub_classic", HubMusic.HUB_CLASSIC,
                "music.global.hub_like", HubMusic.HUB_LIKE,
                "music.global.hubbin", HubMusic.HUBBIN,
                "music.global.island", HubMusic.ISLAND,
                "music.global.our_hub", HubMusic.OUR_HUB,
                "music.global.relax_hub", HubMusic.RELAX_HUB,
                "music.global.we_are", HubMusic.WE_ARE));

        replacements.putAll(Map.of(
                "music.global.parkour_warrior", Game.PARKOUR_WARRIOR_DOJO.getMusicLocation(),
                "music.global.battle_box", Game.BATTLE_BOX.getMusicLocation(),
                "music.global.hole_in_the_wall", Game.HITW.getMusicLocation(),
                "music.global.rocket_spleef", Game.ROCKET_SPLEEF_RUSH.getMusicLocation(),
                "music.global.sky_battle", Game.SKY_BATTLE.getMusicLocation(),
                "music.global.tgttosawaf", Game.TGTTOS.getMusicLocation()));
    }

    @Override
    public SoundInfo apply(SoundInfo info) {
        ResourceLocation replacementPath = replacements.get(info.path().getPath());
        ChatUtils.debug("High Quality music => " + replacementPath);
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

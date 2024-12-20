package net.asodev.islandutils.modules.music.modifiers;

import net.asodev.islandutils.IslandUtils;
import net.asodev.islandutils.modules.music.SoundInfo;
import net.asodev.islandutils.modules.music.TrackMusicModifier;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.util.ChatUtils;
import net.asodev.islandutils.util.IslandSoundEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;

import static net.minecraft.network.chat.Component.literal;
import static net.minecraft.network.chat.Component.translatable;

public class ClassicHitwMusic extends TrackMusicModifier {
    public ClassicHitwMusic(){
        super("music.global.hole_in_the_wall", "classic_hitw.music");
    }

    @Override
    public SoundInfo apply(SoundInfo info) {
        ChatUtils.send(translatable("islandutils.message.music.nowPlaying").withStyle(ChatFormatting.GREEN)
                .append(literal("Spacewall - Taylor Grover").withStyle(ChatFormatting.AQUA))
        );
        return info.withPath(IslandSoundEvents.islandSound("island.music.classic_hitw"));
    }

    @Override
    public boolean hasOption() {
        return false;
    }

    @Override
    public boolean shouldApply1(ResourceLocation soundLocation) {
        return IslandOptions.getClassicHITW().isClassicHITWMusic();
    }
}

package net.asodev.islandutils.mixins.discord;

import net.asodev.islandutils.discord.DiscordPresenceUpdator;
import net.asodev.islandutils.state.MccIslandState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.Score;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(Score.class)
public class ScoreDisplayMixin {

    @Inject(method = "display(Lnet/minecraft/network/chat/Component;)V", at = @At("TAIL"))
    public void setScoreDisplay(Component component, CallbackInfo ci) {
        String playerPrefix = component.getString();

        final Map<String, Pattern> scoreboardPatterns = Map.of(
                "REMAIN", Pattern.compile("PLAYERS REMAINING: (?<remain>([0-9]*/[0-9]*))"),
                "ROUND", Pattern.compile("ROUNDS \\[(?<round>([1-9]*/[1-9]*))]"),
                "MAP", Pattern.compile("MAP: (?<map>\\w+(?:,? \\w+)*)"),
                "MODIFIER", Pattern.compile("MODIFIER: (?<modifier>\\w+(?:,? \\w+)*)"),
                "COURSE", Pattern.compile("COURSE: (?<course>.*)"),
                "LEAP", Pattern.compile("LEAP \\[(?<leap>.*/.*)]")

        );

        for (Map.Entry<String, Pattern> entry : scoreboardPatterns.entrySet()) {
            Matcher matcher = entry.getValue().matcher(playerPrefix);
            if (!matcher.find()) continue;
            String value = matcher.group(1);

            switch (entry.getKey()) {
                case "REMAIN" -> DiscordPresenceUpdator.remainScoreboardUpdate(value, true);
                case "ROUND" -> DiscordPresenceUpdator.roundScoreboardUpdate(value, true);
                case "MAP" -> MccIslandState.setMap(value.toUpperCase()); // Set our MAP
                case "MODIFIER" -> MccIslandState.setModifier(value.toUpperCase()); // Set our MODIFIER
                case "COURSE" -> {
                    DiscordPresenceUpdator.courseScoreboardUpdate(value, true);
                    MccIslandState.setMap(value);
                }
                case "LEAP" -> DiscordPresenceUpdator.leapScoreboardUpdate(value, true);
            }
        }
    }

}

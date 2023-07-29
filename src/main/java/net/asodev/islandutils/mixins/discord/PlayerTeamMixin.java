package net.asodev.islandutils.mixins.discord;

import net.asodev.islandutils.discord.DiscordPresenceUpdator;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.PlayerTeam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(PlayerTeam.class)
public class PlayerTeamMixin {

    // This isn't called in the PacketListenerMixin because for some reason, sometimes things aren't sent with that packet
    // When things are sent all at once, they aren't caught by the packet.
    @Inject(method = "setPlayerPrefix", at = @At("TAIL"))
    public void addPlayerTeam(Component component, CallbackInfo ci) {
        String playerPrefix = component.getString().toUpperCase();

        final Map<String, Pattern> scoreboardPatterns = Map.of(
                "REMAIN", Pattern.compile("PLAYERS REMAINING: (?<remain>([0-9]*/[0-9]*))"),
                "ROUND", Pattern.compile("ROUND \\[(?<round>([1-9]*/[1-9]*))]")
        );

        for (Map.Entry<String, Pattern> entry : scoreboardPatterns.entrySet()) {
            Matcher matcher = entry.getValue().matcher(playerPrefix);
            if (!matcher.find()) continue;
            String value = matcher.group(1);

            switch (entry.getKey()) {
                case "REMAIN" -> DiscordPresenceUpdator.remainScoreboardUpdate(value, true);
                case "ROUND" -> DiscordPresenceUpdator.roundScoreboardUpdate(value, true);
            }
        }
    }

}

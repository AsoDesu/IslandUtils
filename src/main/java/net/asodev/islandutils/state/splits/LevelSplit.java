package net.asodev.islandutils.state.splits;

import net.asodev.islandutils.util.ChatUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.minecraft.network.chat.Component.literal;

public class LevelSplit {
    private static final Pattern channelTitlePattern = Pattern.compile("\\[(.*)]");

    private SplitUI splitUI = new SplitUI(this);
    private Long lastSplitTimestamp = System.currentTimeMillis();
    private String levelName = "M1-1";

    public void handleSubtitle(ClientboundSetSubtitleTextPacket subtitle, CallbackInfo ci) {
        Component component = subtitle.getText();
        String string = component.getString();
        if (string.contains(medalCharacter) && string.length() < 4) {
            modifyMedalTitle(subtitle, ci);
        }
        if (string.startsWith("[")) {
            Matcher matcher = channelTitlePattern.matcher(string);
            if (!matcher.find()) return;
            // This title is sent 1.5s AFTER the level starts, so we need to compensate
            lastSplitTimestamp = System.currentTimeMillis() - 1500;
            levelName = matcher.group(1);
        }
    }
    public void modifyMedalTitle(ClientboundSetSubtitleTextPacket subtitle, CallbackInfo ci) {
        Component component = subtitle.getText();
        Component timeComponent = Component.literal(" ").withStyle(Style.EMPTY)
                .append(Component.literal("(").withStyle(ChatFormatting.GREEN))
                .append(splitUpComponent.copy().withStyle(ChatFormatting.WHITE))
                .append(Component.literal(" -4.20").withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)))
                .append(Component.literal(")").withStyle(ChatFormatting.GREEN));

        Component component1 = component.copy().append(timeComponent);
        Minecraft.getInstance().gui.setSubtitle(component1);
        ci.cancel();

        lastSplitTimestamp = System.currentTimeMillis();
    }

    public double getCurrentSplitTime() {
        return (System.currentTimeMillis() - lastSplitTimestamp) / 1000d;
    }

    public static void onSound(ClientboundSoundPacket clientboundSoundPacket) {
        ResourceLocation soundLoc = clientboundSoundPacket.getSound().value().getLocation();
        String path = soundLoc.getPath();
        if (path.contains("games.parkour_warrior.mode_swap") ||
                path.contains("games.parkour_warrior.restart_course") ||
                path.equals("games.global.timer.round_end") ||
                path.equals("ui.queue_teleport")) {
            // Stop split
            setInstance(null);
            ChatUtils.debug("Ended timer");
        } else if (path.equals("games.global.countdown.go")) {
            setInstance(new LevelSplit());
            ChatUtils.debug("Started timer");
        }
    }

    public SplitUI getUI() {
        return splitUI;
    }
    public String getLevelName() {
        return levelName;
    }

    // Instance stuff
    private static LevelSplit instance;

    public static LevelSplit getInstance() {
        return instance;
    }
    public static void setInstance(LevelSplit instance) {
        LevelSplit.instance = instance;
    }

    // Font stuff
    public static String medalCharacter;
    public static Component splitUpComponent;
    public static Component splitDownComponent;
}

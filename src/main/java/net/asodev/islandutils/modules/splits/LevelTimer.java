package net.asodev.islandutils.modules.splits;

import net.asodev.islandutils.modules.splits.ui.DojoSplitUI;
import net.asodev.islandutils.modules.splits.ui.SplitUI;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.options.categories.SplitsCategory;
import net.asodev.islandutils.state.Game;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.util.ChatUtils;
import net.asodev.islandutils.util.FontUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LevelTimer {
    private static final Pattern channelTitlePattern = Pattern.compile("\\[(.*)]");

    private SplitUI splitUI = null;
    private final LevelSplits splits;
    private Long lastSplitTimestamp = System.currentTimeMillis();

    private String levelName = "M1-1";
    private String levelUid = "";

    private boolean isBetween = true; // If the player is inbetween levels;
    public final SplitsCategory options = IslandOptions.getSplits();

    public LevelTimer(LevelSplits splits) {
        this.splits = splits;
        if (splits != null && splits.getExpires() == null) {
            splits.setExpires(SplitManager.getCurrentCourseExpiry());
        }
        if (options.isShowTimer()) {
            this.splitUI = new DojoSplitUI(this);
        }
    }

    public void handleSubtitle(ClientboundSetSubtitleTextPacket subtitle, CallbackInfo ci) {
        Component component = subtitle.text();
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
            isBetween = false;

            StringBuilder hashString = new StringBuilder();
            for (Component sibling : component.getSiblings()) {
                TextColor color = sibling.getStyle().getColor();
                if (color != null) {
                    hashString.append(color);
                }
            }
            hashString.append(levelName);
            levelUid = hashString.toString();
            ChatUtils.debug("Detected level with id: " + levelUid);
        }
    }
    public void modifyMedalTitle(ClientboundSetSubtitleTextPacket subtitle, CallbackInfo ci) {
        Component component = subtitle.text();
        MutableComponent component1 = component.copy();
        if (options.isShowSplitImprovements()) {
            component1.append(getSplitImprovementComponent());
        }
        Minecraft.getInstance().gui.setSubtitle(component1);
        ci.cancel();

        saveSplit();
        lastSplitTimestamp = System.currentTimeMillis();
        isBetween = true;
    }
    public void saveSplit() {
        if (splits != null) {
            sendSplitCompeteMessage();

            Long millis = getCurrentSplitTimeMilis();
            splits.saveSplit(levelUid, levelName, millis);
        }
    }
    public void sendSplitCompeteMessage() {
        if (!options.isSendSplitTime()) return;
        String time = String.format("%.3fs", getCurrentSplitTime());

        MutableComponent component = Component.literal("[").withStyle(ChatFormatting.GREEN)
                .append(FontUtils.ICON_TICK_SMALL)
                .append("] " + levelName + " complete in: ")
                .append(Component.literal(time).withStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)));
        if (options.isShowSplitImprovements()) {
            component.append(Component.empty().withStyle(ChatFormatting.WHITE).append(getSplitImprovementComponent()));
        }
        ChatUtils.send(component);
    }
    private Component getSplitImprovementComponent() {
        Double splitImprovement = getSplitImprovement();
        if (splitImprovement == null) splitImprovement = 0d;

        String formattedTime = String.format("%.2f", splitImprovement);
        ChatFormatting color;
        Component icon;
        if (splitImprovement > 0) {
            color = ChatFormatting.RED;
            icon = splitDownComponent.copy().withStyle(ChatFormatting.WHITE);
            formattedTime = "+" + formattedTime;
        } else if (splitImprovement < 0) {
            color = ChatFormatting.GREEN;
            icon = splitUpComponent.copy().withStyle(ChatFormatting.WHITE);
        } else {
            color = ChatFormatting.YELLOW;
            icon = Component.literal("-").withStyle(color);
        }

        return Component.literal(" (").withStyle(Style.EMPTY)
                .append(icon)
                .append(Component.literal(" " + formattedTime).withStyle(color))
                .append(Component.literal(")").withStyle(Style.EMPTY));
    }

    public Long getCurrentSplitTimeMilis() {
        return (System.currentTimeMillis() - lastSplitTimestamp);
    }
    public double getCurrentSplitTime() {
        return getCurrentSplitTimeMilis() / 1000d;
    }
    public Double getSplitImprovement() {
        double currentSplitTime = getCurrentSplitTime();
        if (splits == null) {
            return null;
        } else {
            Double split = splits.getSplit(levelUid);
            if (split == null) return null;
            return currentSplitTime - split;
        }
    }

    public static void onSound(ClientboundSoundPacket clientboundSoundPacket) {
        if (!IslandOptions.getSplits().isEnablePkwSplits()) return;
        ResourceLocation soundLoc = clientboundSoundPacket.getSound().value().location();
        String path = soundLoc.getPath();
        boolean isRoundEnd = path.equals("games.global.timer.round_end");
        if (path.contains("games.parkour_warrior.mode_swap") ||
                path.contains("games.parkour_warrior.restart_course") ||
                isRoundEnd ||
                path.equals("ui.queue_teleport")) {
            // Stop split
            LevelTimer currentInstance = getInstance();
            if (currentInstance != null && isRoundEnd) {
                currentInstance.saveSplit();
            }
            setInstance(null);
            ChatUtils.debug("LevelTimer - Ended timer");
        } else if (path.equals("games.global.countdown.go")) {
            LevelSplits splits = null;
            if (MccIslandState.getGame() == Game.PARKOUR_WARRIOR_DOJO) {
                splits = SplitManager.getCourseSplits(MccIslandState.getMap());
            }
            setInstance(new LevelTimer(splits));
            ChatUtils.debug("LevelTimer - Started timer!");
        }
    }

    public static void updateFromConfig(SplitsCategory options) {
        if (!options.isEnablePkwSplits()) setInstance(null);
    }

    public SplitUI getUI() {
        return splitUI;
    }
    public String getLevelName() {
        return levelName;
    }
    public boolean isBetween() {
        return isBetween;
    }

    // Instance stuff
    private static LevelTimer instance;

    public static LevelTimer getInstance() {
        return instance;
    }
    public static void setInstance(LevelTimer instance) {
        LevelTimer.instance = instance;
    }

    // Font stuff
    public static String medalCharacter = "";
    public static Component splitUpComponent = Component.empty();
    public static Component splitDownComponent = Component.empty();
}

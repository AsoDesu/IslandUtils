package net.asodev.islandutils.modules.splits;

import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.options.categories.SplitsCategory;
import net.asodev.islandutils.state.Game;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.modules.splits.ui.DojoSplitUI;
import net.asodev.islandutils.modules.splits.ui.SplitUI;
import net.asodev.islandutils.util.ChatUtils;
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

    // T - Transition from main to main
    // TB - transition from main to bonus
    // U - unfinished bonus level time
    // R - return to main route
    private String levelName = "T0-1";
    private String levelUid = "T0-1";

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
            String nextLevelName = matcher.group(1);
            StringBuilder hashString = new StringBuilder();
            for (Component sibling : component.getSiblings()) {
                TextColor color = sibling.getStyle().getColor();
                if (color != null) {
                    hashString.append(color);
                }
            }
            hashString.append(levelName);
            if (levelName.startsWith("T")) {
                if (nextLevelName.startsWith("B")) {
                    String newName;
                    String newUid;
                    if (nextLevelName.charAt(1) == '4') { // Finale check
                        newName = levelName;
                        String chosenFinale = hashString.toString().split("T")[0];
                        newUid = String.format("%sT3-4", chosenFinale);
                    } else { // Transition to bonus route
                        newName = String.format("TB%c-%c", levelName.charAt(1), levelName.charAt(3));
                        newUid = newName;
                    }
                    levelName = newName;
                    levelUid = newUid;
                }
                saveSplit();
            }
            if (levelName.startsWith("R")) {
                if (nextLevelName.startsWith("B")) return; // Prevent race condition
                saveSplit();
            }
            // This title is sent 1.5s AFTER the level starts, so we need to compensate
            lastSplitTimestamp = System.currentTimeMillis() - 1500;
            levelName = nextLevelName;
            isBetween = false;

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
        Style tickFont = Style.EMPTY.withFont(ResourceLocation.fromNamespaceAndPath("island", "icons")).withColor(ChatFormatting.WHITE);

        MutableComponent component = Component.literal("[").withStyle(ChatFormatting.GREEN)
                .append(Component.literal("\ue009").withStyle(tickFont))
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
        ResourceLocation soundLoc = clientboundSoundPacket.getSound().value().getLocation();
        String path = soundLoc.getPath();
        boolean isRoundEnd = path.equals("games.global.timer.round_end");
        LevelTimer currentInstance = getInstance();
        if (path.contains("games.parkour_warrior.mode_swap") ||
                path.contains("games.parkour_warrior.restart_course") ||
                isRoundEnd ||
                path.equals("ui.queue_teleport")) {
            // Stop split
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
        } else if (path.equals("games.parkour_warrior.medal_gain")
                && currentInstance != null
                && currentInstance.levelName.endsWith("-3")) {
            String newLevelName;
            if (currentInstance.levelName.startsWith("M")) {
                char curLevelName = currentInstance.levelName.charAt(1);
                int curLevelInt = Integer.parseInt(String.valueOf(curLevelName));
                newLevelName = String.format("T%d-%d", curLevelInt, curLevelInt + 1);
            } else {
                newLevelName = "R";
            }
            currentInstance.levelName = newLevelName;
            currentInstance.levelUid = newLevelName;
        } else if (path.equals("games.parkour_warrior.teleport_scroll")
                && currentInstance != null) {
            currentInstance.levelName = "U";
            currentInstance.levelUid = "U";
            currentInstance.saveSplit();
            currentInstance.levelName = "R";
            currentInstance.levelUid = "R";
            currentInstance.lastSplitTimestamp = System.currentTimeMillis();
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

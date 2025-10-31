package net.asodev.islandutils.mixins;

import net.asodev.islandutils.modules.cosmetics.CosmeticChroma;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.util.BarInfo;
import net.asodev.islandutils.util.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Mixin(ItemStack.class)
public class ItemBarMixin {
    @Unique
    private static final Pattern PROGRESS_REGEX = Pattern.compile(".*\\n[\\uE001\\uE272\\uE267\\uE26A\\uE26C\\uE26D\\uE26F\\uE270\\uE273]* (\\d+)%.*");
    @Unique
    private static final Pattern PROGRESS_TRUE_ZERO_REGEX = Pattern.compile(".*\\n[\\uE001\\uE267\\uE26C\\uE26D]* 0%.*");
    @Unique
    private static final String PROGRESS_COSMETIC_LABEL = "Left-Click to Equip\n";
    @Unique
    private static final String PROGRESS_COSMETIC_LABEL_2 = "Left-Click to Unequip\n";
    @Unique
    private static final String PROGRESS_BADGE_EARNED_LABEL = "Click to Display on Profile";
    @Unique
    private static final String PROGRESS_BADGE_INCOMPLETE_LABEL = "\n\nStage incomplete\n\n";
    @Unique
    private static final String PROGRESS_BADGE_PINNED_LABEL = "Shift-Click to Unpin";
    @Unique
    private static final String PROGRESS_BADGE_LOCKED_LABEL = "\nUnlock this slot by reaching the below\namount of Skill Trophies.\n";
    @Unique
    private static final String PROGRESS_OUTFIT_LOCKED_LABEL = "Locked Outfit Slot\n\nSave a loadout of cosmetics and their";
    @Unique
    private static final String PROGRESS_QUEST_LABEL = "\nComplete any of the below tasks to\nfinish the quest.\n";
    @Unique
    private static final String PROGRESS_TOOL_USES_LABEL = "\nUses Remaining: ";
    @Unique
    private static final String PROGRESS_TOOL_LINE_LABEL = "\nYour equipped line adds to your\n";
    @Unique
    private static final String PROGRESS_TOOL_REPAIRABLE_LABEL = "\nThis item is out of uses! You can Repair\nit to restore all its uses, you can only\ndo this once per item.\n";
    @Unique
    private static final String PROGRESS_TOOL_REPAIRED_LABEL = "\nThis item has previously been repaired.\n";
    @Unique
    private static final String PROGRESS_TOOL_BROKEN_LABEL = "\nThis item is out of uses! You've already\nrepaired it once, so cannot do so\nagain.";

    @Unique
    private static final Pattern REPUTATION_REGEX = Pattern.compile(".*\\nRoyal Donations: (\\d+)/(\\d+)\\n.*");

    @Unique
    private static final String CHROMA_LABEL = "\nChromas Unlocked:\n";
    @Unique
    private static final Pattern CHROMA_REGEX = Pattern.compile(".*\\n([\\uE02A\\uE02E\\uE02F\\uE02C\\uE02D\\uE02B]{5}) - (\\d+)\\uE329\\n.*");

    @Unique
    private static final int REPAIRABLE_BAR_COLOR = ARGB.colorFromFloat(1.0F, 1.0F, 0.33F, 0.33F);

    @Unique
    private static final int REPUTATION_BAR_COLOR = ARGB.colorFromFloat(1.0F, 0.66F, 0.33F, 1.0F);

    @Unique
    private static final int CHROMA_BAR_COLOR_SPEED_MS = 2000;

    @Unique
    private Optional<BarInfo> getProgressBar(String lore) {
        if (!IslandOptions.getMisc().isShowProgressBar()) return Optional.empty();

        var isCosmetic = lore.contains(PROGRESS_COSMETIC_LABEL) || lore.contains(PROGRESS_COSMETIC_LABEL_2);
        var isNonEarnedBadge = lore.contains(PROGRESS_BADGE_INCOMPLETE_LABEL) && !lore.contains(PROGRESS_BADGE_EARNED_LABEL);
        var isProfileBadge = lore.contains(PROGRESS_BADGE_PINNED_LABEL) || lore.contains(PROGRESS_BADGE_LOCKED_LABEL);
        var isOutfitSlot = lore.contains(PROGRESS_OUTFIT_LOCKED_LABEL);
        if (isCosmetic || isNonEarnedBadge || isProfileBadge || isOutfitSlot) return Optional.empty();

        var isBrokenTool = lore.contains(PROGRESS_TOOL_BROKEN_LABEL);
        if (isBrokenTool) return Optional.of(new BarInfo(Fraction.ZERO, ARGB.alpha(0)));

        var isRepairableTool = lore.contains(PROGRESS_TOOL_REPAIRABLE_LABEL);
        if (isRepairableTool) return Optional.of(new BarInfo(Fraction.ONE_HALF, REPAIRABLE_BAR_COLOR));

        var progresses = getProgresses(lore);
        if (progresses.isEmpty()) return Optional.empty();

        var isQuest = lore.contains(PROGRESS_QUEST_LABEL);
        var fraction = isQuest ? progresses.stream().max(Fraction::compareTo) : progresses.stream().findFirst();

        var isNonBrokenTool = lore.contains(PROGRESS_TOOL_USES_LABEL);
        if (isNonBrokenTool) {
            return fraction.map(f -> {
                var canRepairTool = !lore.contains(PROGRESS_TOOL_LINE_LABEL);
                if (canRepairTool) {
                    var isRepairedTool = lore.contains(PROGRESS_TOOL_REPAIRED_LABEL);
                    f = (isRepairedTool ? f : f.add(Fraction.ONE)).divideBy(Fraction.getFraction(2, 1));
                }
                var color = Mth.hsvToRgb(f.floatValue() / 3.0F, 1.0F, 1.0F);
                return new BarInfo(f, color);
            });
        }

        return fraction.map(f -> new BarInfo(f, IslandOptions.getMisc().getProgressBarColorARGB()));
    }

    @Unique
    private static @NotNull ArrayList<Fraction> getProgresses(String lore) {
        var matcher = PROGRESS_REGEX.matcher(lore);
        var progresses = new ArrayList<Fraction>();
        while (matcher.find()) {
            var percentage = Integer.parseInt(matcher.group(1));
            if (percentage == 0) {
                var trueZeroMatcher = PROGRESS_TRUE_ZERO_REGEX.matcher(matcher.group(0));
                if (!trueZeroMatcher.matches()) {
                    percentage = 1;
                }
            }

            var fraction = Fraction.getFraction(percentage, 100);
            progresses.add(fraction);
        }
        return progresses;
    }

    @Unique
    private Optional<BarInfo> getReputationBar(String lore) {
        if (!IslandOptions.getCosmetics().isShowReputationBar()) return Optional.empty();

        var matcher = REPUTATION_REGEX.matcher(lore);
        if (!matcher.find()) return Optional.empty();

        var level = Integer.parseInt(matcher.group(1));
        var max = Integer.parseInt(matcher.group(2));

        try {
            var fraction = Fraction.getFraction(level, max);
            return Optional.of(new BarInfo(fraction, REPUTATION_BAR_COLOR));
        } catch (ArithmeticException e) {
            return Optional.empty();
        }
    }

    @Unique
    private Optional<BarInfo> getChromaBar(String lore) {
        if (!IslandOptions.getCosmetics().isShowChromaBar()) return Optional.empty();

        if (!lore.contains(CHROMA_LABEL)) return Optional.empty();

        var matcher = CHROMA_REGEX.matcher(lore);
        if (!matcher.find()) return Optional.empty();
        var chromaChars = Optional.of(matcher.group(1).chars().mapToObj(c -> (char) c).toList());

        return chromaChars.map(charList ->
            charList.stream()
                .map(CosmeticChroma::fromChar)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList()
        ).map(chromas -> {
            var fraction = CosmeticChroma.toFraction(chromas);

            var color = 0;
            if (!chromas.isEmpty()) {
                var currentChromaIdx = (int) ((System.currentTimeMillis() / CHROMA_BAR_COLOR_SPEED_MS) % chromas.size());
                var nextChromaIdx = (currentChromaIdx + 1) % chromas.size();

                var currentChromaColor = chromas.get(currentChromaIdx).toColor();
                var nextChromaColor = chromas.get(nextChromaIdx).toColor();

                var interpolatedProgress = (float) (System.currentTimeMillis() % CHROMA_BAR_COLOR_SPEED_MS) / CHROMA_BAR_COLOR_SPEED_MS;
                color = ARGB.lerp(interpolatedProgress, currentChromaColor, nextChromaColor);
            }

            return new BarInfo(fraction, color);
        });
    }

    @Unique
    private Optional<BarInfo> getItemBar() {
        var itemStack = (ItemStack)(Object)this;
        var lore = Utils.getTooltipLines(itemStack);
        if (lore == null) return Optional.empty();

        var loreStr = lore.stream().map(Component::getString).collect(Collectors.joining("\n"));

        return this.getChromaBar(loreStr)
            .or(() -> this.getReputationBar(loreStr))
            .or(() -> this.getProgressBar(loreStr));
    }

    @Inject(method = "isBarVisible", at = @At("RETURN"), cancellable = true)
    private void injectBarVisibility(CallbackInfoReturnable<Boolean> cir) {
        if (this.getItemBar().isPresent()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getBarWidth", at = @At("RETURN"), cancellable = true)
    private void injectBarWidth(CallbackInfoReturnable<Integer> cir) {
        this.getItemBar().ifPresent(barInfo -> cir.setReturnValue(barInfo.getBarWidth()));
    }

    @Inject(method = "getBarColor", at = @At("RETURN"), cancellable = true)
    private void injectBarColor(CallbackInfoReturnable<Integer> cir) {
        this.getItemBar().ifPresent(barInfo -> cir.setReturnValue(barInfo.color()));
    }
}

package net.asodev.islandutils.mixins;

import net.asodev.islandutils.modules.cosmetics.CosmeticChroma;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.util.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Mixin(ItemStack.class)
public class ItemBarMixin {
    @Unique
    private static final Pattern PROGRESS_REGEX = Pattern.compile("^[\\uE001\\uE269\\uE26C\\uE266]* (\\d+)%$");
    @Unique
    private static final String PROGRESS_COSMETIC_LABEL = "Left-Click to Equip";
    @Unique
    private static final String PROGRESS_BADGE_PINNED_LABEL = "Shift-Click to Unpin";
    @Unique
    private static final String PROGRESS_BADGE_LOCKED_LABEL = "Unlock this slot by reaching the below";
    @Unique
    private static final String PROGRESS_QUEST_LABEL = "finish the quest.";

    @Unique
    private static final Pattern REPUTATION_REGEX = Pattern.compile("^Royal Donations: (\\d+)/(\\d+)$");

    @Unique
    private static final String CHROMA_LABEL = "Chromas Unlocked:";
    @Unique
    private static final Pattern CHROMA_REGEX = Pattern.compile("^([\\uE02A\\uE02E\\uE02F\\uE02C\\uE02D\\uE02B]{5}) - (\\d+)\\uE328$");

    @Unique
    private static final int PROGRESS_BAR_COLOR = ARGB.colorFromFloat(1.0F, 0.33F, 1.0F, 0.33F);

    @Unique
    private static final int REPUTATION_BAR_COLOR = ARGB.colorFromFloat(1.0F, 0.66F, 0.33F, 1.0F);

    @Unique
    private static final int CHROMA_BAR_COLOR_SPEED_MS = 2000;

    @Unique
    private Optional<Fraction> getProgress() {
        var itemStack = (ItemStack)(Object)this;
        var lore = Utils.getLores(itemStack);
        if (lore == null) return Optional.empty();

        var isCosmetic = lore.stream().anyMatch(line -> line.getString().contains(PROGRESS_COSMETIC_LABEL));
        var isProfileBadge = lore.stream().map(Component::getString).anyMatch(
            line -> line.contains(PROGRESS_BADGE_PINNED_LABEL) || line.contains(PROGRESS_BADGE_LOCKED_LABEL)
        );
        if (isCosmetic || isProfileBadge) return Optional.empty();

        var progresses = lore.stream().<Optional<Fraction>>map(line -> {
            var matcher = PROGRESS_REGEX.matcher(line.getString());
            if (!matcher.find()) return Optional.empty();

            var percentage = Integer.parseInt(matcher.group(1));
            return Optional.of(Fraction.getFraction(percentage, 100));
        }).filter(Optional::isPresent).map(Optional::get);

        var isQuest = lore.stream().anyMatch(line -> line.getString().contains(PROGRESS_QUEST_LABEL));
        if (isQuest) {
            return progresses.max(Fraction::compareTo);
        } else {
            return progresses.findFirst();
        }
    }

    @Unique
    private Optional<Fraction> getReputation() {
        var itemStack = (ItemStack)(Object)this;
        var lore = Utils.getLores(itemStack);
        if (lore == null) return Optional.empty();

        return lore.stream().<Optional<Fraction>>map(line -> {
            var matcher = REPUTATION_REGEX.matcher(line.getString());
            if (!matcher.find()) return Optional.empty();

            var level = Integer.parseInt(matcher.group(1));
            var max = Integer.parseInt(matcher.group(2));

            try {
                var fraction = Fraction.getFraction(level, max);
                return Optional.of(fraction);
            } catch (ArithmeticException e) {
                return Optional.empty();
            }
        }).filter(Optional::isPresent).map(Optional::get).findFirst();
    }

    @Unique
    private Optional<List<CosmeticChroma>> getChromas() {
        var itemStack = (ItemStack)(Object)this;
        var lore = Utils.getLores(itemStack);
        if (lore == null) return Optional.empty();

        if (lore.stream().noneMatch(it -> it.getString().equals(CHROMA_LABEL))) return Optional.empty();

        return lore.stream().<Optional<List<CosmeticChroma>>>map(line -> {
            var matcher = CHROMA_REGEX.matcher(line.getString());
            if (!matcher.find()) return Optional.empty();

            var chromaChars = matcher.group(1).chars().mapToObj(c -> (char)c);
            var chromaList = chromaChars
                    .map(CosmeticChroma::fromChar)
                    .filter(Optional::isPresent).map(Optional::get)
                    .toList();
            return Optional.of(chromaList);
        }).filter(Optional::isPresent).map(Optional::get).findFirst();
    }

    @Inject(
            method = "isBarVisible",
            at = @At("RETURN"),
            cancellable = true
    )
    private void injectBarVisibility(CallbackInfoReturnable<Boolean> cir) {
        if (IslandOptions.getMisc().isShowProgressBar()) {
            this.getProgress().ifPresent(progress -> cir.setReturnValue(true));
        }
        if (IslandOptions.getCosmetics().isShowReputationBar()) {
            this.getReputation().ifPresent(reputation -> cir.setReturnValue(true));
        }
        if (IslandOptions.getCosmetics().isShowChromaBar()) {
            this.getChromas().ifPresent(chroma -> cir.setReturnValue(true));
        }
    }

    @Inject(
            method = "getBarWidth",
            at = @At("RETURN"),
            cancellable = true
    )
    private void injectBarWidth(CallbackInfoReturnable<Integer> cir) {
        if (IslandOptions.getMisc().isShowProgressBar()) {
            this.getProgress().ifPresent(progress ->
                cir.setReturnValue(Math.min(Mth.mulAndTruncate(progress, 13), 13))
            );
        }
        if (IslandOptions.getCosmetics().isShowReputationBar()) {
            this.getReputation().ifPresent(reputation ->
                cir.setReturnValue(Math.min(Mth.mulAndTruncate(reputation, 13), 13))
            );
        }
        if (IslandOptions.getCosmetics().isShowChromaBar()) {
            this.getChromas().ifPresent(chromas ->
                cir.setReturnValue(Math.min(Mth.mulAndTruncate(CosmeticChroma.toFraction(chromas), 13), 13))
            );
        }
    }

    @Inject(
            method = "getBarColor",
            at = @At("RETURN"),
            cancellable = true
    )
    private void injectBarColor(CallbackInfoReturnable<Integer> cir) {
        if (IslandOptions.getMisc().isShowProgressBar()) {
            if (this.getProgress().isPresent()) {
                cir.setReturnValue(PROGRESS_BAR_COLOR);
            }
        }
        if (IslandOptions.getCosmetics().isShowReputationBar()) {
            if (this.getReputation().isPresent()) {
                cir.setReturnValue(REPUTATION_BAR_COLOR);
            }
        }
        if (IslandOptions.getCosmetics().isShowChromaBar()) {
            this.getChromas().ifPresent(chromas -> {
                if (chromas.isEmpty()) return;

                var currentChromaIdx = (int) ((System.currentTimeMillis() / CHROMA_BAR_COLOR_SPEED_MS) % chromas.size());
                var nextChromaIdx = (currentChromaIdx + 1) % chromas.size();

                var currentChromaColor = chromas.get(currentChromaIdx).toColor();
                var nextChromaColor = chromas.get(nextChromaIdx).toColor();

                var interpolatedProgress = (float) (System.currentTimeMillis() % CHROMA_BAR_COLOR_SPEED_MS) / CHROMA_BAR_COLOR_SPEED_MS;
                var interpolatedColor = ARGB.lerp(interpolatedProgress, currentChromaColor, nextChromaColor);

                cir.setReturnValue(interpolatedColor);
            });
        }
    }
}

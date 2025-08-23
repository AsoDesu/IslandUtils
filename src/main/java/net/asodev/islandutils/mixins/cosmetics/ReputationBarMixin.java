package net.asodev.islandutils.mixins.cosmetics;

import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.util.Utils;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.regex.Pattern;

@Mixin(ItemStack.class)
public class ReputationBarMixin {
    @Unique
    private static final Pattern REPUTATION_REGEX = Pattern.compile("^Royal Donations: (\\d+)/(\\d+)$");

    @Unique
    private static final int REPUTATION_BAR_COLOR = ARGB.colorFromFloat(1.0F, 0.66F, 0.33F, 1.0F);

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

    @Inject(
            method = "isBarVisible",
            at = @At("RETURN"),
            cancellable = true
    )
    private void injectBarVisibility(CallbackInfoReturnable<Boolean> cir) {
        if (!IslandOptions.getCosmetics().isShowReputationBar()) return;
        this.getReputation().ifPresent(reputation -> cir.setReturnValue(true));
    }

    @Inject(
            method = "getBarWidth",
            at = @At("RETURN"),
            cancellable = true
    )
    private void injectBarWidth(CallbackInfoReturnable<Integer> cir) {
        if (!IslandOptions.getCosmetics().isShowReputationBar()) return;
        this.getReputation().ifPresent(reputation ->
            cir.setReturnValue(Math.min(Mth.mulAndTruncate(reputation, 13), 13))
        );
    }

    @Inject(
            method = "getBarColor",
            at = @At("RETURN"),
            cancellable = true
    )
    private void injectBarColor(CallbackInfoReturnable<Integer> cir) {
        if (!IslandOptions.getCosmetics().isShowReputationBar()) return;
        if (this.getReputation().isPresent()) {
            cir.setReturnValue(REPUTATION_BAR_COLOR);
        }
    }
}

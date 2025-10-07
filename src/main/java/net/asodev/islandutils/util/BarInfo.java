package net.asodev.islandutils.util;

import net.minecraft.util.Mth;
import org.apache.commons.lang3.math.Fraction;

public record BarInfo(Fraction fraction, int color) {
    public int getBarWidth() {
        if (fraction.compareTo(Fraction.ZERO) > 0) {
            return Math.min(1 + Mth.mulAndTruncate(fraction, 12), 13);
        } else {
            return Math.min(Mth.mulAndTruncate(fraction, 13), 13);
        }
    }
}

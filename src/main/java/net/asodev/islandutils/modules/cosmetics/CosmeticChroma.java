package net.asodev.islandutils.modules.cosmetics;

import net.minecraft.util.ARGB;
import org.apache.commons.lang3.math.Fraction;

import java.util.List;
import java.util.Optional;

public enum CosmeticChroma {
    MECHANICAL,
    NATURAL,
    OCEANIC,
    MAGICAL,
    GRAYSCALE;

    public static final int COUNT = 5;

    public static Optional<CosmeticChroma> fromChar(char c) {
        return Optional.ofNullable(switch (c) {
            case '\uE02E' -> MECHANICAL;
            case '\uE02F' -> NATURAL;
            case '\uE02C' -> OCEANIC;
            case '\uE02D' -> MAGICAL;
            case '\uE02B' -> GRAYSCALE;
            default -> null;
        });
    }
    
    public int toColor() {
        return switch (this) {
            case MECHANICAL -> ARGB.colorFromFloat(1.0f, 1.0f, 0.33f, 0.0f);
            case NATURAL -> ARGB.colorFromFloat(1.0f, 0.0f, 0.66f, 0.33f);
            case OCEANIC -> ARGB.colorFromFloat(1.0f, 0.0f, 0.5f, 1.0f);
            case MAGICAL -> ARGB.colorFromFloat(1.0f, 0.5f, 0.0f, 1.0f);
            case GRAYSCALE -> ARGB.colorFromFloat(1.0f, 0.5f, 0.5f, 0.5f);
        };
    }

    public static Fraction toFraction(List<CosmeticChroma> chromas) {
        return Fraction.getFraction(chromas.size(), COUNT);
    }
}

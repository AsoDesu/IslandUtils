package net.asodev.islandutils.modules.cosmetics;

import com.google.common.collect.EnumHashBiMap;
import com.google.common.collect.ImmutableMap;
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

    private static final char EMPTY_CHAR = '\uE02A';
    private static final EnumHashBiMap<CosmeticChroma, Character> CHARS = EnumHashBiMap.create(
        ImmutableMap.of(
            MECHANICAL, '\uE02E',
            NATURAL, '\uE02F',
            OCEANIC, '\uE02C',
            MAGICAL, '\uE02D',
            GRAYSCALE, '\uE02B'
        )
    );
    public static final String CHAR_REGEX =
            "[" + EMPTY_CHAR + String.join("", CHARS.values().stream().map(String::valueOf).toList()) + "]";

    public static final int COUNT = CHARS.size();

    public final char toChar() {
        return Optional.ofNullable(CHARS.get(this)).orElse(EMPTY_CHAR);
    }

    public static Optional<CosmeticChroma> fromChar(char c) {
        return Optional.ofNullable(CHARS.inverse().get(c));
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

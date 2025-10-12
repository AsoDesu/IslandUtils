package net.asodev.islandutils.modules.music;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class MusicModifier {
    private boolean isEnabled = defaultOption();
    private final String identifier;
    private final Component name;
    private final Component desc;

    public MusicModifier(String identifier) {
        this.identifier = identifier;
        this.name = Component.translatable("islandutils.music_modifier." + identifier);
        this.desc = Component.translatableWithFallback("islandutils.music_modifier." + identifier + ".desc", "");
    }

    @Override
    public String toString() {
        return identifier;
    }

    public abstract SoundInfo apply(SoundInfo info);
    public abstract boolean shouldApply(ResourceLocation soundLocation);

    public boolean hasOption() {
        return true;
    }
    public boolean defaultOption() {
        return true;
    }

    public final boolean isEnabled() {
        return isEnabled;
    }
    public final void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public final String identifier() {
        return identifier;
    }
    public final Component name() {
        return name;
    }
    public Component desc() {
        return desc;
    }
}

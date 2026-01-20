package net.asodev.islandutils.modules.music;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public abstract class TrackMusicModifier extends MusicModifier {
    private String trackPath;

    public TrackMusicModifier(String trackPath, String identifier) {
        super(identifier);
        this.trackPath = trackPath;
    }

    @Override
    public boolean shouldApply(Identifier soundLocation) {
        return soundLocation.getPath().equals(trackPath) && shouldApply1(soundLocation);
    }

    public boolean shouldApply1(Identifier soundLocation) {
        return true;
    }
}

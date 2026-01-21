package net.asodev.islandutils.modules;

import com.noxcrew.noxesium.core.fabric.mcc.MccNoxesiumEntrypoint;

public class NoxesiumEntrypoint extends MccNoxesiumEntrypoint {
    @Override
    public void initialize() {
        new NoxesiumIntegration().init();
    }
}

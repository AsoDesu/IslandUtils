package net.asodev.islandutils.options.categories;

import dev.isxander.yacl3.api.ConfigCategory;
import net.asodev.islandutils.state.splits.SplitType;

public class SplitsCategory implements OptionsCategory {

    boolean enablePkwSplits = false;
    boolean sendSplitTime = true;
    boolean showTimer = true;
    boolean showSplitImprovements = true;
    SplitType saveMode = SplitType.BEST;

    public boolean isEnablePkwSplits() {
        return enablePkwSplits;
    }

    public boolean isSendSplitTime() {
        return sendSplitTime;
    }

    public boolean isShowTimer() {
        return showTimer;
    }

    public boolean isShowSplitImprovements() {
        return showSplitImprovements;
    }

    public SplitType getSaveMode() {
        return saveMode;
    }

    @Override
    public ConfigCategory getCategory() {
        return null;
    }
}

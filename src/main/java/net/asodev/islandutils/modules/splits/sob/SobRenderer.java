package net.asodev.islandutils.modules.splits.sob;

import net.asodev.islandutils.modules.splits.LevelSplits;
import net.asodev.islandutils.modules.splits.SplitManager;
import net.asodev.islandutils.state.MccIslandState;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.scores.PlayerTeam;

public class SobRenderer {
    private static final int SOB_COLOR = 0x32cd32;

    private final TeamInfo teamInfo;

    public SobRenderer(TeamInfo teamInfo) {
        this.teamInfo = teamInfo;
    }

    public void update() {
        LevelSplits splits = SplitManager.getCourseSplits(MccIslandState.getMap());
        SobCalc.standardTime(splits).ifPresent(t -> updateTeamWithTime(teamInfo.standard(), t));
        SobCalc.advancedTime(splits).ifPresent(t -> updateTeamWithTime(teamInfo.advanced(), t.time()));
        SobCalc.expertTime(splits).ifPresent(t -> updateTeamWithTime(teamInfo.expert(), t));
    }

    private Component getOriginalComp(PlayerTeam from) {
        Component ogPrefix = from.getPlayerPrefix();
        MutableComponent newPrefix = Component.empty();
        for (Component c : ogPrefix.getSiblings()) {
            if (c.getString().contains("(")) continue;
            newPrefix.append(c);
        }
        return newPrefix;
    }

    private void updateTeamWithTime(PlayerTeam team, long time) {
        MutableComponent timeComp = Component
                .literal(SobCalc.format(time))
                .withColor(SOB_COLOR);
        team.setPlayerPrefix(Component.empty().append(getOriginalComp(team)).append(timeComp));
    }

}

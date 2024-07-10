package net.asodev.islandutils.modules.splits.sob;

import net.minecraft.world.scores.*;

import java.util.Optional;

public record TeamInfo(PlayerTeam standard, PlayerTeam advanced, PlayerTeam expert) {
    private static final int STANDARD = 10, ADVANCED = 8, EXPERT = 6;

    public static Optional<TeamInfo> fromScoreboard(Scoreboard scoreboard) {
        String STDName, ADVName, EXPName;
        STDName = ADVName = EXPName = null;
        for (ScoreHolder scoreHolder : scoreboard.getTrackedPlayers()) {
            String name = scoreHolder.getScoreboardName();
            for (Objective objective : scoreboard.getObjectives()) {
                ReadOnlyScoreInfo info = scoreboard.getPlayerScoreInfo(scoreHolder, objective);
                if (info == null) continue;
                switch (info.value()) {
                    case STANDARD -> STDName = name;
                    case ADVANCED -> ADVName = name;
                    case EXPERT -> EXPName = name;
                }
            }
        }
        if (STDName == null || ADVName == null || EXPName == null) return Optional.empty();
        PlayerTeam STDTeam, ADVTeam, EXPTeam;
        STDTeam = ADVTeam = EXPTeam = null;
        for (PlayerTeam team : scoreboard.getPlayerTeams()) {
            for (String playerName : team.getPlayers()) {
                if (playerName.equals(STDName)) {
                    STDTeam = team;
                } else if (playerName.equals(ADVName)) {
                    ADVTeam = team;
                } else if (playerName.equals(EXPName)) {
                    EXPTeam = team;
                }
            }
        }
        if (STDTeam == null || ADVTeam == null || EXPTeam == null) return Optional.empty();
        return Optional.of(new TeamInfo(STDTeam, ADVTeam, EXPTeam));
    }
}

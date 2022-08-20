package net.asodev.islandutils.state;

public class MccIslandState {

    private static STATE game = STATE.HUB;

    public static STATE getGame() {
        return game;
    }
    public static void setGame(STATE game) {
        MccIslandState.game = game;
    }
}

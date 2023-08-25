package net.asodev.islandutils.modules.plobby;

import net.asodev.islandutils.modules.plobby.state.PlobbyStateProvider;

public class Plobby {
    PlobbyStateProvider stateProvider;
    PlobbyUI ui;
    public Plobby(PlobbyStateProvider stateProvider) {
        this.stateProvider = stateProvider;
        this.ui = new PlobbyUI(stateProvider);

        stateProvider.setCodeUpdateCallback(this::onOpen);
        stateProvider.setLockStateCallback(this::onLockStateChange);
    }

    public void onOpen(String code) {

    }
    public void onLockStateChange(Boolean state) {

    }
    public void onDisband() {

    }

    public PlobbyUI getUi() {
        return ui;
    }

    private static Plobby instance = null;
    public static void create(PlobbyStateProvider state) {
        instance = new Plobby(state);
    }
    public static void disband() {
        instance.onDisband();
        instance = null;
    }

    public static Plobby getInstance() {
        return instance;
    }
}

package net.asodev.islandutils.modules.plobby.state;

import java.util.function.Consumer;

public interface PlobbyStateProvider {
    boolean isLocked();
    boolean hasJoinCode();
    String getJoinCode();

    void setCodeUpdateCallback(Consumer<String> consumer);
    void setLockStateCallback(Consumer<Boolean> consumer);
}

package net.asodev.islandutils.modules.plobby.state;

import net.asodev.islandutils.IslandUtilsEvents;
import net.asodev.islandutils.modules.plobby.Plobby;
import net.asodev.islandutils.modules.plobby.PlobbyFeatures;
import net.asodev.islandutils.util.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class GlobalPlobbyState implements PlobbyStateProvider {

    private boolean locked = true;
    private String joinCode = null;
    private Consumer<String> codeUpdateCallback = null;
    private Consumer<Boolean> lockSateCallback = null;

    public void setLocked(boolean locked) {
        this.locked = locked;
        if (lockSateCallback != null)
            lockSateCallback.accept(locked);
    }

    public void setJoinCode(String joinCode) {
        this.joinCode = joinCode;
        if (codeUpdateCallback != null)
            codeUpdateCallback.accept(joinCode);
    }

    public void analyseItem(int slot, ItemStack item) {
        if (item == null || item.is(Items.AIR)) return;

        ResourceLocation itemId = Utils.getCustomItemID(item);
        if (itemId == null) return;

        switch (slot) {
            case 8 -> {
                if (!itemId.getPath().equals("island_interface.generic.blank")) return;
                String code = PlobbyFeatures.getJoinCodeFromItem(item);
                this.setJoinCode(code);
            }
            case 47 -> {
                boolean isLocked = itemId.getPath().endsWith("unlock_lobby");
                this.setLocked(isLocked);

                boolean isOwner = !itemId.getPath().endsWith("unlock_lobby_disabled");
                if (isOwner && !Plobby.hasInstance()) {
                    Plobby.create(this);
                } else if (!isOwner && Plobby.hasInstance()) {
                    Plobby.disband();
                }
            }
        }
    }

    static GlobalPlobbyState instance = null;
    public static void register() {
        Plobby.saveCodeToFile("");
        IslandUtilsEvents.JOIN_MCCI.register(() -> {
            instance = new GlobalPlobbyState();
        });
        IslandUtilsEvents.QUIT_MCCI.register(() -> {
            Plobby.disband();
            instance = null;
        });
        IslandUtilsEvents.CHAT_MESSAGE.register((message, modify) -> {
            String content = message.content().getString();
            if (content.equalsIgnoreCase("Your Private Lobby has been disbanded.")) {
                Plobby.disband();
            }
        });
    }
    @Nullable
    public static GlobalPlobbyState getInstance() {
        return instance;
    }

    @Override
    public boolean isLocked() {
        return locked;
    }
    @Override
    public boolean hasJoinCode() {
        return joinCode != null;
    }
    @Override
    public String getJoinCode() {
        return joinCode;
    }
    @Override
    public void setCodeUpdateCallback(Consumer<String> consumer) {
        codeUpdateCallback = consumer;
    }
    @Override
    public void setLockStateCallback(Consumer<Boolean> consumer) {
        lockSateCallback = consumer;
    }
}

package net.asodev.islandutils.modules.plobby.state;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.asodev.islandutils.modules.plobby.Plobby;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.util.function.Consumer;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class DebugPlobbyState implements PlobbyStateProvider {
    public boolean locked = true;
    public String joinCode = null;
    public Consumer<String> codeUpdateCallback = null;
    public Consumer<Boolean> lockSateCallback = null;

    public static LiteralArgumentBuilder<FabricClientCommandSource> getDebugCommand() {
        DebugPlobbyState state = new DebugPlobbyState();
        var openBuilder = literal("open")
                .executes((ctx) -> {
                    state.joinCode = null;
                    state.locked = true;
                    Plobby.create(state);
                    return 1;
                });
        var setJoinCode = literal("setcode")
                .then(argument("code", StringArgumentType.string())
                .executes(ctx -> {
                    state.joinCode = ctx.getArgument("code", String.class);
                    state.updateCodeCallback(state.joinCode);
                    return 1;
                }));
        var setLocked = literal("setlocked")
                .then(argument("locked", BoolArgumentType.bool())
                .executes(ctx -> {
                    state.locked = ctx.getArgument("locked", Boolean.class);
                    state.lockStateCallback(state.locked);
                    return 1;
                }));
        var disband = literal("disband")
                .executes(ctx -> {
                    Plobby.disband();
                   return 1;
                });

        return literal("plobby")
                .then(openBuilder)
                .then(setJoinCode)
                .then(disband)
                .then(setLocked);
    }

    public void updateCodeCallback(String string) {
        if (codeUpdateCallback != null) codeUpdateCallback.accept(string);
    }
    public void lockStateCallback(Boolean bl) {
        if (lockSateCallback != null) lockSateCallback.accept(bl);
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

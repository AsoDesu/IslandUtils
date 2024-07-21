package net.asodev.islandutils.modules.splits;

import net.asodev.islandutils.modules.splits.sob.AdvancedInfo;
import net.asodev.islandutils.modules.splits.sob.SobCalc;
import net.asodev.islandutils.state.Game;
import net.asodev.islandutils.state.MccIslandState;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.network.chat.Component;

import java.util.Optional;

public class SplitCommands {
    public SplitCommands() {
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("showadvancedpath").executes(context -> {
            if (MccIslandState.getGame() != Game.PARKOUR_WARRIOR_DOJO) {
                context.getSource().sendFeedback(Component.literal("Not in PKWD"));
                return 0;
            }
            LevelSplits splits = SplitManager.getCourseSplits(MccIslandState.getMap());
            Optional<AdvancedInfo> advInfo = SobCalc.advancedTime(splits);
            if (advInfo.isEmpty()) {
                context.getSource().sendFeedback(Component.literal("Could not get advanced path"));
                return 0;
            }
            context.getSource().sendFeedback(Component.literal(advInfo.get().path()));
            return 1;
        }))));
    }
}

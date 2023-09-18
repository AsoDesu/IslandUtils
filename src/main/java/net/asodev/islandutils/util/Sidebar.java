package net.asodev.islandutils.util;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class Sidebar {

    @Nullable
    public static Component getSidebarName() {
        Objective sidebar = getSidebar();
        return sidebar != null ? sidebar.getDisplayName() : null;
    }

    public static List<Component> getSidebarLines() {
        Scoreboard scoreboard = getScoreboard();
        if (scoreboard == null) return Collections.emptyList();
        Objective sidebar = getSidebar(scoreboard);
        if (sidebar == null) return Collections.emptyList();

        return scoreboard.getPlayerScores(sidebar).stream().map((score) -> {
            String owner = score.getOwner();
            PlayerTeam playerTeam = scoreboard.getPlayersTeam(owner);
            return PlayerTeam.formatNameForTeam(playerTeam, Component.literal(owner));
        }).collect(Collectors.toList());
    }

    public static int findLine(Predicate<Component> predicate) {
        return findLine(predicate, getSidebarLines());
    }
    public static int findLine(Predicate<Component> predicate, List<Component> lines) {
        int i = 0;
        for (Component line : lines) {
            if (predicate.test(line)) return i;
            i++;
        }
        return -1;
    }

    @Nullable
    public static Component getLine(List<Component> lines, int line) {
        if (lines.size() < line) return null;
        return lines.get(line);
    }

    @Nullable
    private static Objective getSidebar() {
        Scoreboard scoreboard = getScoreboard();
        return scoreboard != null ? getSidebar(scoreboard) : null;
    }

    @Nullable
    private static Objective getSidebar(Scoreboard scoreboard) {
        return scoreboard.getDisplayObjective(Scoreboard.DISPLAY_SLOT_SIDEBAR);
    }

    @Nullable
    private static Scoreboard getScoreboard() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) return null;
        return minecraft.level.getScoreboard();
    }

}

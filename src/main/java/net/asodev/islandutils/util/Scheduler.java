package net.asodev.islandutils.util;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Scheduler {
    private final List<Task> tasks = new ArrayList<>();

    public Scheduler() {
        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            if (tasks.isEmpty()) return;
            tasks.removeIf(s -> s.tick(client));
        });
    }

    static class Task {
        private boolean shouldRemove = false;
        private int ticks;
        private final Consumer<Minecraft> callback;

        public Task(int ticks, Consumer<Minecraft> callback) {
            this.ticks = ticks;
            this.callback = callback;
        }

        public boolean tick(Minecraft client) {
            if (shouldRemove) return true;

            ticks--;
            if (ticks > 0) return false;
            callback.accept(client);
            return true;
        }

        public void cancel() {
            shouldRemove = true;
        }
    }

    private static Scheduler instance;

    public static Task schedule(int afterTicks, Consumer<Minecraft> callback) {
        Task task = new Task(afterTicks, callback);
        instance.tasks.add(task);
        return task;
    }
    public static Task nextTick(Consumer<Minecraft> callback) {
        return schedule(1, callback);
    }
    public static void create() {
        instance = new Scheduler();
    }

}

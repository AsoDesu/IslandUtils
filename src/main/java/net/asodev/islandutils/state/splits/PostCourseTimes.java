package net.asodev.islandutils.state.splits;

import net.asodev.islandutils.util.ChatUtils;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;

public class PostCourseTimes {

    public static void triggerSendMessage() {
        Style buttonStyle = Style.EMPTY.withFont(new ResourceLocation("island", "icons"))
                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "islandutils view_split_times"));
        Component component = Component.literal("Course Split Times: ")
                .append(Component.literal("\ue006").withStyle(buttonStyle));
        ChatUtils.send(component);
    }

    public static void sendSplitTimes() {

    }

}

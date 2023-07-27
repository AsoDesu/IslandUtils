package net.asodev.islandutils.state.splits;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.network.chat.Component.literal;

public class LevelSplit {
    private SplitUI splitUI = new SplitUI();

    public SplitUI getUI() {
        return splitUI;
    }

    public void handleSubtitle(ClientboundSetSubtitleTextPacket subtitle, CallbackInfo ci) {
        Component component = subtitle.getText();
        String string = component.getString();
        if (!string.contains(medalCharacter) || string.length() > 4) return;

        Component timeComponent = Component.literal(" ").withStyle(Style.EMPTY)
                .append(Component.literal("(").withStyle(ChatFormatting.GREEN))
                .append(splitUpComponent.copy().withStyle(ChatFormatting.WHITE))
                .append(Component.literal(" -4.20").withStyle(Style.EMPTY.withColor(ChatFormatting.GREEN)))
                .append(Component.literal(")").withStyle(ChatFormatting.GREEN));


        Component component1 = component.copy().append(timeComponent);
        Minecraft.getInstance().gui.setSubtitle(component1);
        ci.cancel();
    }

    private static LevelSplit instance = new LevelSplit();

    public static LevelSplit getInstance() {
        return instance;
    }
    public static void setInstance(LevelSplit instance) {
        LevelSplit.instance = instance;
    }

    public static String medalCharacter;
    public static Component splitUpComponent;
    public static Component splitDownComponent;
}

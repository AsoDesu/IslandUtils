package net.asodev.islandutils.mixins.ui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.asodev.islandutils.mixins.ui.CommandSuggestionsAccessor;
import net.asodev.islandutils.util.ChatUtils;
import net.asodev.islandutils.util.PlainTextButtonNoShadow;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

import static net.asodev.islandutils.state.MccIslandState.isOnline;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin extends Screen {
    @Shadow CommandSuggestions commandSuggestions;

    protected ChatScreenMixin(Component component) { super(component); }

    private final List<PlainTextButtonNoShadow> buttons = new ArrayList<>();
    private final Style style = Style.EMPTY.withColor(ChatFormatting.WHITE).withFont(new ResourceLocation("island","icons"));
    private int nextPress = 0;

    @Inject(method = "init", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        if (!isOnline()) return;

        int x = 2;
        buttons.add(new PlainTextButtonNoShadow(
                x,
                this.height - 14 - 9 - 2,
                43,
                9,
                Component.literal("\ue002").withStyle(style),
                (d) -> Minecraft.getInstance().getConnection().sendCommand("chat local"),
                Minecraft.getInstance().font
        ));
        x += 43 + 3;
        buttons.add(new PlainTextButtonNoShadow(
                x,
                this.height - 14 - 9 - 2,
                43,
                9,
                Component.literal("\ue003").withStyle(style),
                (d) -> Minecraft.getInstance().getConnection().sendCommand("chat party"),
                Minecraft.getInstance().font
        ));
        x += 43 + 3;
        buttons.add(new PlainTextButtonNoShadow(
                x,
                this.height - 14 - 9 - 2,
                43,
                9,
                Component.literal("\ue004").withStyle(style),
                (d) -> Minecraft.getInstance().getConnection().sendCommand("chat team"),
                Minecraft.getInstance().font
        ));
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void render(PoseStack poseStack, int i, int j, float f, CallbackInfo ci) {
        CommandSuggestionsAccessor suggestionsAccessor = ((CommandSuggestionsAccessor)commandSuggestions);
        if (suggestionsAccessor.suggestions() == null && suggestionsAccessor.commandUsage().size() == 0) {
            buttons.forEach(btn -> btn.render(poseStack, i, j, f));
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        if (nextPress > 0) nextPress--;
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void mouseClicked(double d, double e, int i, CallbackInfoReturnable<Boolean> cir) {
        if (nextPress > 0) return;
        for (PlainTextButtonNoShadow button : buttons) {
            if (button.isHoveredOrFocused()) {
                button.onPress();
                nextPress = 20;
                cir.setReturnValue(true);
                return;
            }
        }
    }

}

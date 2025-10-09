package net.asodev.islandutils.mixins.ui;

import net.asodev.islandutils.modules.ChatChannelButton;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.util.PlainTextButtonNoShadow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

import static net.asodev.islandutils.state.MccIslandState.isOnline;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin extends Screen {
    @Shadow
    CommandSuggestions commandSuggestions;

    protected ChatScreenMixin(Component component) {
        super(component);
    }

    @Unique
    private final List<PlainTextButtonNoShadow> buttons = new ArrayList<>();
    @Unique
    private long lastPress = 0;

    @Unique
    private static final int BUTTON_WIDTH = 43;
    @Unique
    private static final int BUTTON_HEIGHT = 9;

    @Inject(method = "init", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        if (!isOnline() || !IslandOptions.getMisc().showChannelSwitchers()) return;
        buttons.clear();

        int x = 2;
        final var y = this.height - 14 - BUTTON_HEIGHT - 2;

        for (final var button : ChatChannelButton.currentButtons()) {
            final var buttonWidget = new PlainTextButtonNoShadow(
                    x,
                    y,
                    BUTTON_WIDTH,
                    BUTTON_HEIGHT,
                    button.text(),
                    (d) -> Minecraft.getInstance().getConnection().sendCommand("chat " + button.name()),
                    Minecraft.getInstance().font
            );
            buttons.add(buttonWidget);
            addWidget(buttonWidget);

            x += BUTTON_WIDTH + 3;
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void render(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) {
        CommandSuggestionsAccessor suggestionsAccessor = ((CommandSuggestionsAccessor) commandSuggestions);
        if (suggestionsAccessor.suggestions() == null && suggestionsAccessor.commandUsage().size() == 0) {
            buttons.forEach(btn -> btn.render(guiGraphics, i, j, f));
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void mouseClicked(double d, double e, int i, CallbackInfoReturnable<Boolean> cir) {
        long timeNow = System.currentTimeMillis();
        if ((timeNow - lastPress) < 1000) return;
        for (PlainTextButtonNoShadow button : buttons) {
            if (button.isHoveredOrFocused()) {
                button.onPress();
                lastPress = System.currentTimeMillis();
                cir.setReturnValue(true);
                return;
            }
        }
    }

}

package dev.asodesu.islandutils.mixin;

import dev.asodesu.islandutils.api.extentions.MinecraftExtKt;
import dev.asodesu.islandutils.features.chatbuttons.ChatButtons;
import dev.asodesu.islandutils.features.chatbuttons.button.ChannelButton;
import dev.asodesu.islandutils.features.chatbuttons.button.ChatButtonWidget;
import dev.asodesu.islandutils.options.MiscOptions;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ChatScreen.class)
public abstract class AddChatChannelButtonsMixin extends Screen {
    @Shadow protected EditBox input;
    @Shadow private CommandSuggestions commandSuggestions;
    @Unique private List<AbstractWidget> buttonWidgets = List.of();

    protected AddChatChannelButtonsMixin(Component component) {
        super(component);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        if (!MinecraftExtKt.isOnline() || !MiscOptions.INSTANCE.getChatChannelButtons().get()) return;
        ChatButtons chatButtons = ChatButtons.INSTANCE;

        // (2) - the x position of the edit box background (source: ChatScreen#render - guiGraphics#fill)
        int x = 2;
        // (this.height - 14) - the y position of the edit box background (source: ChatScreen#render - guiGraphics#fill)
        int y = (this.height - 14) - chatButtons.getBUTTON_HEIGHT() - chatButtons.getINPUT_GAP();

        List<AbstractWidget> widgets = new ArrayList<>();
        for (ChannelButton button : chatButtons.currentButtons()) {
            AbstractWidget widget = button.widget();
            widget.setX(x);
            widget.setY(y);

            x += widget.getWidth() + chatButtons.getBUTTON_GAP();
            widgets.add(widget);
            addWidget(widget);
        }

        buttonWidgets = widgets;
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void render(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) {
        // hide when command suggestions is visible
        if (commandSuggestions.isVisible()) return;
        for (AbstractWidget widget : buttonWidgets) {
            widget.render(guiGraphics, i, j, f);
        }
    }

}

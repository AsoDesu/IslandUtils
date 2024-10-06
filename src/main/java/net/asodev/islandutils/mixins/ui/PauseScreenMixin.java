package net.asodev.islandutils.mixins.ui;

import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.state.MccIslandState;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PauseScreen.class)
public class PauseScreenMixin extends Screen {

    @Mutable @Shadow private void onDisconnect() {}

    protected PauseScreenMixin(Component component) {
        super(component);
    }

    @Redirect(
            method = "createPauseMenu",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/Button;builder(Lnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/Button$OnPress;)Lnet/minecraft/client/gui/components/Button$Builder;"
            )
    )
    private Button.Builder createPause(Component component, Button.OnPress onPress) {
        if (!MccIslandState.isOnline() || !IslandOptions.getMisc().isPauseConfirm()) return Button.builder(component, onPress);
        if (component == CommonComponents.GUI_DISCONNECT) {
            Component message = Component.literal("Are you sure you want to leave?").withStyle(ChatFormatting.AQUA);
            Component no = Component.literal("Cancel");
            Component yes = Component.literal("Disconnect").withStyle(ChatFormatting.RED);

            return Button.builder(component, (button) -> {
                ConfirmScreen screen = new ConfirmScreen((bool) -> callback(bool, button), Component.literal("Leave the server"), message, yes, no);
                Minecraft.getInstance().setScreen(screen);
            });
        }
        return Button.builder(component, onPress);
    }

    void callback(boolean confirm, Button buttonx) {
        if (confirm) disconnect(buttonx);
        else { Minecraft.getInstance().setScreen(new PauseScreen(true)); }
    }

    void disconnect(Button button) {
        button.active = false;
        this.minecraft.getReportingContext().draftReportHandled(this.minecraft, this, this::onDisconnect, true);
    }

}

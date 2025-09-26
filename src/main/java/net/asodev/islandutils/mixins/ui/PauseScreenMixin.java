package net.asodev.islandutils.mixins.ui;

import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.state.MccIslandState;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.minecraft.client.gui.screens.PauseScreen.disconnectFromWorld;

@Mixin(PauseScreen.class)
public class PauseScreenMixin extends Screen {

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
            Component title = Component.translatable("islandutils.message.disconnectWarnTitle");
            Component message = Component.translatable("islandutils.message.disconnectWarn").withStyle(ChatFormatting.AQUA);
            Component no = Component.translatable("islandutils.message.disconnectWarnCancel");
            Component yes = Component.translatable("islandutils.message.disconnectWarnDisconnect").withStyle(ChatFormatting.RED);

            return Button.builder(component, (button) -> {
                ConfirmScreen screen = new ConfirmScreen((bool) -> callback(bool, button), title, message, yes, no);
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
        this.minecraft.getReportingContext().draftReportHandled(this.minecraft, this, () -> disconnectFromWorld(this.minecraft, ClientLevel.DEFAULT_QUIT_MESSAGE), true);
    }
}

package net.asodev.islandutils.mixins;

import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.state.MccIslandState;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.FrameWidget;
import net.minecraft.client.gui.components.GridWidget;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(PauseScreen.class)
public class PauseScreenMixin extends Screen {

    @Final @Shadow private static Component DISCONNECT;
    @Final @Shadow private static Component RETURN_TO_MENU;
    @Mutable @Shadow private Button disconnectButton;
    @Mutable @Shadow private void onDisconnect() {}

    protected PauseScreenMixin(Component component) {
        super(component);
    }

    @Inject(method = "createPauseMenu",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;isLocalServer()Z", shift = At.Shift.AFTER),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void createPause(CallbackInfo ci, GridWidget gridWidget, GridWidget.RowHelper rowHelper) {
        if (!MccIslandState.isOnline()) return;
        ci.cancel();

        Component message = Component.literal("Are you sure you want to leave?").withStyle(ChatFormatting.AQUA);
        Component no = Component.literal("Cancel");
        Component yes = Component.literal("Disconnect").withStyle(ChatFormatting.RED);

        Component component = this.minecraft.isLocalServer() ? RETURN_TO_MENU : DISCONNECT;
        this.disconnectButton = rowHelper.addChild(Button.builder(component, (button) -> {
            if (!IslandOptions.getOptions().isPauseConfirm()) disconnect(button);
            else {
                ConfirmScreen screen = new ConfirmScreen((bool) -> callback(bool, button), Component.literal("Leave the server"), message, yes, no);
                Minecraft.getInstance().setScreen(screen);
            }
        }).width(204).build(), 2);
        gridWidget.pack();
        FrameWidget.alignInRectangle(gridWidget, 0, 0, this.width, this.height, 0.5F, 0.25F);
        this.addRenderableWidget(gridWidget);
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

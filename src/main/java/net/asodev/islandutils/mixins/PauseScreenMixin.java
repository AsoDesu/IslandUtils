package net.asodev.islandutils.mixins;

import com.mojang.realmsclient.RealmsMainScreen;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.state.MccIslandState;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PauseScreen.class)
public class PauseScreenMixin extends Screen {

    protected PauseScreenMixin(Component component) {
        super(component);
    }

    @Inject(method = "createPauseMenu", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;isLocalServer()Z", shift = At.Shift.AFTER), cancellable = true)
    private void createPause(CallbackInfo ci) {
        if (!MccIslandState.isOnline()) return;
        ci.cancel();

        // This has to be `menu.disconnect` and is replaced in the lang mixin.
        // i do this because ReplayMod looks for this menu button using it's lang in order to place it's buttons
        // Replay mod is common, and i like compatibility!
        Component title = Component.translatable("menu.disconnect");
        Component message = Component.literal("Are you sure you want to leave?").withStyle(ChatFormatting.AQUA);
        Component no = Component.literal("Cancel");
        Component yes = Component.literal("Disconnect").withStyle(ChatFormatting.RED);
        this.addRenderableWidget(new Button(this.width / 2 - 102, this.height / 4 + 120 + -16, 204, 20, title, (buttonx) -> {
            if (!IslandOptions.getOptions().isPauseConfirm()) disconnect(buttonx);
            else {
                ConfirmScreen screen = new ConfirmScreen((bool) -> callback(bool, buttonx), Component.literal("Leave the server"), message, yes, no);
                Minecraft.getInstance().setScreen(screen);
            }
        }));
    }

    void callback(boolean confirm, Button buttonx) {
        if (confirm) disconnect(buttonx);
        else { Minecraft.getInstance().setScreen(new PauseScreen(true)); }
    }

    void disconnect(Button buttonx) {
        TitleScreen titleScreen = new TitleScreen();
        buttonx.active = false;
        this.minecraft.level.disconnect();
        this.minecraft.clearLevel();
        this.minecraft.setScreen(new JoinMultiplayerScreen(titleScreen));
    }

}

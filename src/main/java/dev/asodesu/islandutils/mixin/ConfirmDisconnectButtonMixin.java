package dev.asodesu.islandutils.mixin;

import dev.asodesu.islandutils.api.extentions.MinecraftExtKt;
import dev.asodesu.islandutils.features.ConfirmDisconnectScreen;
import dev.asodesu.islandutils.options.MiscOptions;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(PauseScreen.class)
public abstract class ConfirmDisconnectButtonMixin extends Screen {
    protected ConfirmDisconnectButtonMixin(Component component) {
        super(component);
    }

    // god i love mixins
    @Redirect(
            method = "createPauseMenu",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/Button;builder(Lnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/Button$OnPress;)Lnet/minecraft/client/gui/components/Button$Builder;"
            ),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;isLocalServer()Z"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/GridLayout;arrangeElements()V")
            )
    )
    private Button.Builder buildButton(Component component, Button.OnPress disconnectOnPress) {
        if (!MinecraftExtKt.isOnline() || !MiscOptions.INSTANCE.getConfirmDisconnect().get())
            return Button.builder(component, disconnectOnPress);

        return Button.builder(component, (button -> {
            PauseScreen pauseScreen = (PauseScreen)(Object)this;
            minecraft.setScreen(new ConfirmDisconnectScreen(pauseScreen, disconnectOnPress));
        }));
    }

}

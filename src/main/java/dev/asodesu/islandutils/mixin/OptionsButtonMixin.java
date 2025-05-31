package dev.asodesu.islandutils.mixin;

import dev.asodesu.islandutils.options.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public class OptionsButtonMixin extends Screen {

    protected OptionsButtonMixin(Component component) {
        super(component);
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        this.addRenderableWidget(
                Button.builder(Component.translatable("menu.islandutils"), (btn) -> {
                    this.minecraft.setScreen(Options.INSTANCE.getScreen(this));
                }).pos(10, 10).size(100, 20).build()
        );
    }

}

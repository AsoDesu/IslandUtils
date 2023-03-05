package net.asodev.islandutils.mixins.ui;

import net.asodev.islandutils.options.IslandOptions;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public class OptionsMixin extends Screen {

    protected OptionsMixin(Component component) {
        super(component);
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        this.addRenderableWidget(Button.builder(Component.translatable("menu.island_utils"), (button) -> {
            this.minecraft.setScreen(IslandOptions.getScreen(this));
        }).pos(10, 10).size(100, 20).build());
    }

}

package net.asodev.islandutils.mixins;

import net.asodev.islandutils.options.IslandOptions;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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
        this.addRenderableWidget(new Button(this.width / 2 - 155, this.height / 6 + 144 - 6, 150, 20, Component.literal("IslandUtils Options"), (button) -> {
            this.minecraft.setScreen(IslandOptions.getScreen(this));
        }));
    }

}

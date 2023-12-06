package net.asodev.islandutils.mixins;

import net.asodev.islandutils.IslandUtils;
import net.asodev.islandutils.util.ChatUtils;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.asodev.islandutils.IslandUtils.packUpdater;

@Mixin(TitleScreen.class)
public abstract class MainScreenMixin extends Screen {

    protected MainScreenMixin(Component component) { super(component); }

    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        if (IslandUtils.availableUpdate == null) return;

        Component text = Component.literal(ChatUtils.translate("&6⚠ &eIsland Utils Update Available! &n&lClick to Update!&e &6⚠"));

        this.addRenderableWidget(new PlainTextButton(
                (this.width / 2 - 100) + ((200 - this.font.width(text)) / 2),
                (this.height / 4 + 48 + 24 * 2) + 24,
                this.font.width(text),
                10,
                text,
                (button) -> Util.getPlatform().openUri(IslandUtils.availableUpdate.releaseUrl()),
                this.font));
    }
}

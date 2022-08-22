package net.asodev.islandutils.mixins;

import com.google.common.util.concurrent.Runnables;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.realmsclient.gui.screens.RealmsCreateRealmScreen;
import com.mojang.realmsclient.gui.screens.RealmsSubscriptionInfoScreen;
import net.asodev.islandutils.IslandUtils;
import net.minecraft.Util;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(TitleScreen.class)
public class MainScreenMixin extends Screen {

    protected MainScreenMixin(Component component) { super(component); }

    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        if (IslandUtils.availableUpdate == null) return;
        Component text = Component.literal("Island Utils Update Available!");

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

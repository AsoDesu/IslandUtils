package net.asodev.islandutils.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.asodev.islandutils.IslandUtils;
import net.asodev.islandutils.mixins.resources.ProgressScreenAccessor;
import net.asodev.islandutils.util.ChatUtils;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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

    @Inject(method = "render", at = @At("TAIL"))
    private void render(PoseStack poseStack, int i, int j, float f, CallbackInfo ci) {
        if (!packUpdater.getting) return;

        float progress = 0;
        if (packUpdater.state != null) progress = ((ProgressScreenAccessor) packUpdater.state).getProgress() / 100f;

        int backgroundColor = 0x60000000;
        int foregroundColor = 0xFFFFFFFF;

        int width = 150;
        int height = 10;

        fill(poseStack, (this.width / 2) - width, 3, (this.width / 2) + width, 5+height, backgroundColor);
        fill(poseStack, (this.width / 2) - width, 3, (int) ((this.width / 2) - width + (Math.min(100, progress) * (width * 2))), 3+height, foregroundColor);
        drawCenteredString(poseStack, this.font, Component.literal((int)(progress * 100) + "%"), this.width / 2, 3 + 10 + 3, 0xFFFFFF);
    }
}

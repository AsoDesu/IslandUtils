package dev.asodesu.islandutils.mixin.chest;

import dev.asodesu.islandutils.Modules;
import dev.asodesu.islandutils.api.chest.analysis.ChestAnalyser;
import dev.asodesu.islandutils.api.chest.analysis.ContainerScreenHelper;
import dev.asodesu.islandutils.api.chest.font.ChestBackgrounds;
import dev.asodesu.islandutils.api.extentions.MinecraftExtKt;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.List;

@Mixin(AbstractContainerScreen.class)
public class ChestScreenInterceptorMixin implements ContainerScreenHelper {
    @Shadow @Nullable protected Slot hoveredSlot;
    @Shadow protected int imageWidth;
    @Shadow protected int imageHeight;
    @Unique @Nullable ChestAnalyser analyser;
    @Unique Collection<Identifier> menuComponents = List.of();

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(AbstractContainerMenu abstractContainerMenu, Inventory inventory, Component component, CallbackInfo ci) {
        if (!MinecraftExtKt.isOnline()) return;

        menuComponents = ChestBackgrounds.INSTANCE.get(component);
        analyser = Modules.INSTANCE.getChestAnalysis().createAnalyser(menuComponents);
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void render(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) {
        if (analyser != null) {
            analyser.render(guiGraphics, i, j, this);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        if (analyser != null) analyser.tick(this);
    }

    @Inject(method = "renderSlot", at = @At("HEAD"))
    private void renderHead(GuiGraphics guiGraphics, Slot slot, int i, int j, CallbackInfo ci) {
        if (analyser != null) analyser.renderSlotBack(guiGraphics, this, slot);
    }

    @Inject(method = "renderSlot", at = @At("TAIL"))
    private void renderTail(GuiGraphics guiGraphics, Slot slot, int i, int j, CallbackInfo ci) {
        if (analyser != null) analyser.renderSlotFront(guiGraphics, this, slot);
    }

    @Inject(method = "mouseDragged", at = @At("HEAD"))
    private void mouseDragged(MouseButtonEvent mouseButtonEvent, double dragX, double dragY, CallbackInfoReturnable<Boolean> cir) {
        if (analyser != null) analyser.mouseDragged(this, mouseButtonEvent.x(), mouseButtonEvent.y(), dragX, dragY);
    }

    @Inject(method = "mouseReleased", at = @At("TAIL"))
    private void mouseReleased(MouseButtonEvent mouseButtonEvent, CallbackInfoReturnable<Boolean> cir) {
        if (analyser != null) analyser.mouseReleased(this, mouseButtonEvent.x(), mouseButtonEvent.y(), mouseButtonEvent.button());
    }

    @Inject(method = "keyPressed", at = @At("TAIL"))
    private void keyPressed(KeyEvent keyEvent, CallbackInfoReturnable<Boolean> cir) {
        if (analyser != null) analyser.keyPressed(this, keyEvent.key(), keyEvent.scancode(), keyEvent.modifiers());
    }

    @Inject(method = "onClose", at = @At("HEAD"))
    private void onClose(CallbackInfo ci) {
        if (analyser != null) analyser.close(this);
    }

    @Unique
    @Override
    public @Nullable ChestAnalyser getAnalyser() {
        return analyser;
    }

    @Unique
    @Override
    public @NotNull Collection<Identifier> getMenuComponents() {
        return menuComponents;
    }

    @Unique
    @Override
    public @Nullable Slot getHoveredSlot() {
        return hoveredSlot;
    }

    @Unique
    @Override
    public @NotNull AbstractContainerScreen<?> getScreen() {
        return (AbstractContainerScreen<?>)(Object)this;
    }

    @Unique
    @Override
    public int getImageWidth() {
        return imageWidth;
    }

    @Unique
    @Override
    public int getImageHeight() {
        return imageHeight;
    }
}

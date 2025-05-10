package dev.asodesu.islandutils.mixin.chest;

import dev.asodesu.islandutils.Modules;
import dev.asodesu.islandutils.api.chest.analysis.ChestAnalyser;
import dev.asodesu.islandutils.api.chest.analysis.ContainerScreenHelper;
import dev.asodesu.islandutils.api.chest.font.ChestBackgrounds;
import dev.asodesu.islandutils.api.extentions.MinecraftExtKt;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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

import java.util.Collection;
import java.util.List;

@Mixin(AbstractContainerScreen.class)
public class ChestScreenInterceptorMixin implements ContainerScreenHelper {
    @Shadow @Nullable protected Slot hoveredSlot;
    @Unique @Nullable ChestAnalyser analyser;
    @Unique Collection<ResourceLocation> menuComponents = List.of();

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(AbstractContainerMenu abstractContainerMenu, Inventory inventory, Component component, CallbackInfo ci) {
        if (!MinecraftExtKt.isOnline()) return;

        menuComponents = ChestBackgrounds.INSTANCE.get(component);
        analyser = Modules.INSTANCE.getChestAnalysis().createAnalyser(menuComponents);
    }

    @Inject(
            method = "render",
            at = @At("TAIL")
    )
    private void render(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) {
        if (analyser != null) analyser.render(guiGraphics, this);
    }

    @Inject(
            method = "renderSlot",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V",
                    shift = At.Shift.AFTER
            )
    )
    private void render(GuiGraphics guiGraphics, Slot slot, CallbackInfo ci) {
        if (analyser != null) analyser.renderSlot(guiGraphics, this, slot);
    }

    @Unique
    @Override
    public @Nullable ChestAnalyser getAnalyser() {
        return analyser;
    }

    @Unique
    @Override
    public @NotNull Collection<ResourceLocation> getMenuComponents() {
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
}

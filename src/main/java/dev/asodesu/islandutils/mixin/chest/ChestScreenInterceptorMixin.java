package dev.asodesu.islandutils.mixin.chest;

import dev.asodesu.islandutils.Modules;
import dev.asodesu.islandutils.api.MinecraftExtKt;
import dev.asodesu.islandutils.api.chest.analysis.ChestAnalyser;
import dev.asodesu.islandutils.api.chest.analysis.ContainerScreenMixinHelper;
import dev.asodesu.islandutils.api.chest.font.ChestBackgrounds;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;

@Mixin(AbstractContainerScreen.class)
public class ChestScreenInterceptorMixin implements ContainerScreenMixinHelper {
    @Unique @Nullable ChestAnalyser analyser;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(AbstractContainerMenu abstractContainerMenu, Inventory inventory, Component component, CallbackInfo ci) {
        if (!MinecraftExtKt.isOnline()) return;

        Collection<ResourceLocation> resourceLocations = ChestBackgrounds.INSTANCE.get(component);
        analyser = Modules.INSTANCE.getChestAnalysis().createAnalyser(resourceLocations);
    }

    @Unique
    public @Nullable ChestAnalyser getAnalyser() {
        return analyser;
    }
}

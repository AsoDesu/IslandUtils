package dev.asodesu.islandutils.mixin.chest;

import dev.asodesu.islandutils.api.chest.analysis.ChestAnalyser;
import dev.asodesu.islandutils.api.chest.analysis.ContainerScreenMixinHelper;
import dev.asodesu.islandutils.api.extentions.MinecraftExtKt;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(AbstractContainerMenu.class)
public class ChestMenuInterceptorMixin {

    @Shadow @Final private static Logger LOGGER;

    @Inject(method = "initializeContents", at = @At("TAIL"))
    private void initalizeContents(int stateId, List<ItemStack> list, ItemStack itemStack, CallbackInfo ci) {
        if (!MinecraftExtKt.isOnline()) return;

        ChestAnalyser analyser = getScreenChestAnalyser();
        if (analyser == null) return;

        int index = 0;
        for (ItemStack newStack : list) {
            try {
                analyser.analyse(newStack, index);
            } catch (Exception e) {
                LOGGER.error("Failed to run ChestAnalysis on item", e);
            }
            index++;
        }
    }

    @Inject(method = "setItem", at = @At("TAIL"))
    private void setItem(int slot, int stateId, ItemStack itemStack, CallbackInfo ci) {
        if (!MinecraftExtKt.isOnline()) return;

        ChestAnalyser analyser = getScreenChestAnalyser();
        if (analyser == null) return;

        analyser.analyse(itemStack, slot);
    }

    @Nullable
    @Unique
    private ChestAnalyser getScreenChestAnalyser() {
        Screen screen = Minecraft.getInstance().screen;
        if (screen instanceof AbstractContainerScreen<?> containerScreen) {
            if (containerScreen.getMenu() != (Object)this) return null;
        }
        if (!(screen instanceof ContainerScreenMixinHelper helper)) return null;
        return helper.getAnalyser();
    }

}

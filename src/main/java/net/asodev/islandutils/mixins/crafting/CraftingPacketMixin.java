package net.asodev.islandutils.mixins.crafting;

import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.state.crafting.CraftingMenuType;
import net.asodev.islandutils.state.crafting.CraftingUI;
import net.asodev.islandutils.util.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(AbstractContainerMenu.class)
public abstract class CraftingPacketMixin {

    public CraftingPacketMixin() {

    }

    @Inject(method = "initializeContents", at = @At("TAIL"))
    private void init(int i, List<ItemStack> list, ItemStack itemStack, CallbackInfo ci) {
        if (IslandOptions.getOptions().isDebugMode()) {
            for (int j = 0; j < list.size(); ++j) {
                injectedSetItem(j, 0, list.get(j), ci);
            }
        }
    }

    @Inject(method = "setItem", at = @At("TAIL"))
    private void injectedSetItem(int slot, int j, ItemStack item, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance(); // TODO: Add online check
        if (minecraft.screen instanceof ContainerScreen container) {
            Component uiTitle = container.getTitle();
            CraftingMenuType type = CraftingUI.craftingMenuType(uiTitle);
            if (type == null) return;

            CraftingUI.analyseCraftingItem(type, item, slot);
        }
    }

}

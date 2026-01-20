package net.asodev.islandutils.mixins.plobby;

import net.asodev.islandutils.modules.plobby.PlobbyJoinCodeCopy;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.util.Utils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public class PlobbyChestMixin extends Screen {
    protected PlobbyChestMixin(Component component) {
        super(component);
    }

    @Inject(method = "slotClicked", at = @At("HEAD"))
    private void slotClicked(Slot slot, int index, int j, ClickType clickType, CallbackInfo ci) {
        if (!MccIslandState.isOnline()) return;
        if (slot == null) return;
        if (index != 8 && slot.hasItem()) return;

        ItemStack item = slot.getItem();
        Identifier customItemID = Utils.getCustomItemID(item);
        if (customItemID != null && !customItemID.getPath().equals(Utils.BLANK_ITEM_ID)) return;

        String code = PlobbyJoinCodeCopy.getJoinCodeFromItem(item);
        if (code == null) return;

        PlobbyJoinCodeCopy.lastCopy = System.currentTimeMillis(); // We copied rn
        this.minecraft.keyboardHandler.setClipboard(code);
    }

}

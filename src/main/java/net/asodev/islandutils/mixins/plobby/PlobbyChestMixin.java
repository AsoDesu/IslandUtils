package net.asodev.islandutils.mixins.plobby;

import com.sun.net.httpserver.HttpServer;
import net.asodev.islandutils.modules.plobby.Plobby;
import net.asodev.islandutils.modules.plobby.PlobbyFeatures;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.util.ChatUtils;
import net.asodev.islandutils.util.Utils;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(AbstractContainerScreen.class)
public class PlobbyChestMixin extends Screen {
    protected PlobbyChestMixin(Component component) {
        super(component);
    }


    @Inject(method = "slotClicked", at = @At("HEAD"))
    private void slotClicked(Slot slot, int index, int j, ClickType clickType, CallbackInfo ci) {
        if (!MccIslandState.isOnline()) return;
        if (!this.getTitle().contains(Plobby.getTitleComponent())) return;
        if (slot == null) return;
        if (index != 8 && slot.hasItem()) return;

        String code = PlobbyFeatures.getJoinCodeFromItem(slot.getItem());
        if (code == null) return;

        PlobbyFeatures.lastCopy = System.currentTimeMillis(); // We copied rn
        this.minecraft.keyboardHandler.setClipboard(code);
    }

}

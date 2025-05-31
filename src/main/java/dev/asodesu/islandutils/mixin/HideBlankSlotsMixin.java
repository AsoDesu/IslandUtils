package dev.asodesu.islandutils.mixin;

import dev.asodesu.islandutils.api.chest.ItemExtKt;
import dev.asodesu.islandutils.api.extentions.MinecraftExtKt;
import dev.asodesu.islandutils.api.game.GameExtKt;
import dev.asodesu.islandutils.games.Hub;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public abstract class HideBlankSlotsMixin {

    @Shadow public abstract ItemStack getItem();

    @Inject(method = "isHighlightable", at = @At("HEAD"), cancellable = true)
    private void isHighlightable(CallbackInfoReturnable<Boolean> cir) {
        if (!MinecraftExtKt.isOnline()) return;
        if (!GameExtKt.getInLobbyOrFishing()) return;

        ItemStack item = this.getItem();
        ResourceLocation customItemId = ItemExtKt.getCustomItemId(item);
        if (customItemId == null) cir.setReturnValue(!item.isEmpty());
        else if (customItemId.getPath().equals("island_interface.generic.blank")) cir.setReturnValue(false);
    }

}

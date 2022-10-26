package net.asodev.islandutils.mixins.cosmetics;

import com.google.gson.JsonObject;
import net.asodev.islandutils.state.CosmeticState;
import net.minecraft.client.gui.font.providers.BitmapProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;

import static net.asodev.islandutils.state.CosmeticState.MCC_ICONS;

@Mixin(BitmapProvider.Builder.class)
public class FontLoaderMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(ResourceLocation resourceLocation, int i, int j, List<int[]> list, CallbackInfo ci) {
        if (list.size() == 1) {
            int[] c = list.get(0);

            StringBuilder builder = new StringBuilder();
            for (int point : c) { builder.appendCodePoint(point); }

            Component comp = Component.literal(builder.toString()).setStyle(Style.EMPTY.withFont(MCC_ICONS));
            switch (resourceLocation.getPath()) {
                case "_fonts/tooltips/hat.png" -> CosmeticState.HAT_COMP = comp;
                case "_fonts/tooltips/accessory.png" -> CosmeticState.ACCESSORY_COMP = comp;
                case "_fonts/tooltips/hair.png" -> CosmeticState.HAIR_COMP = comp;
            }
        }
    }

}

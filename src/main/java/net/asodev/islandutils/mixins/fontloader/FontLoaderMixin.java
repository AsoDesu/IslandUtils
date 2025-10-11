package net.asodev.islandutils.mixins.fontloader;

import net.asodev.islandutils.fontloader.FontLoaderManager;
import net.minecraft.client.gui.font.providers.BitmapProvider;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BitmapProvider.Definition.class)
public class FontLoaderMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(ResourceLocation resourceLocation, int i, int j, int[][] chars, CallbackInfo ci) {
        if (chars.length == 1) {
            int[] c = chars[0];

            StringBuilder builder = new StringBuilder();
            for (int point : c) builder.appendCodePoint(point);

            FontLoaderManager.tryFulfillAsset(resourceLocation, builder.toString());
        }
    }
}

package dev.asodesu.islandutils.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
import dev.asodesu.islandutils.api.DebugExtKt;
import dev.asodesu.islandutils.api.chest.font.FontCollection;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.client.gui.font.providers.GlyphProviderDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

@Mixin(FontManager.class)
public class FontLoaderMixin {

    @Inject(
            method = "method_51623",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/datafixers/util/Pair;getFirst()Ljava/lang/Object;",
                    shift = At.Shift.AFTER
            )
    )
    private void inject(
            Map.Entry entry,
            ResourceLocation fontKey,
            ResourceManager resourceManager,
            Executor executor,
            CallbackInfoReturnable<FontManager.UnresolvedBuilderBundle> cir,
            @Local Pair<Object, GlyphProviderDefinition.Conditional> pair
    ) {
        FontCollection.INSTANCE.add(fontKey, pair.getSecond().definition());
    }

}

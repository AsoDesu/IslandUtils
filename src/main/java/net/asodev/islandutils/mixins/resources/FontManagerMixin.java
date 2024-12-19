package net.asodev.islandutils.mixins.resources;

import com.mojang.blaze3d.font.GlyphProvider;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.client.gui.font.providers.GlyphProviderDefinition;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(FontManager.class)
public class FontManagerMixin {
    @Inject(method = "safeLoad", at = @At("HEAD"), cancellable = true)
    public void load(FontManager.BuilderId builderId, GlyphProviderDefinition.Loader loader, ResourceManager resourceManager, Executor executor, CallbackInfoReturnable<CompletableFuture<Optional<GlyphProvider>>> cir) {
        if (builderId.pack().equals("islandutils")) {
            cir.setReturnValue(CompletableFuture.supplyAsync(() -> {
                try {
                    return Optional.of(loader.load(resourceManager));
                } catch (Exception exception) {
                    return Optional.empty();
                }
            }, executor));
        }
    }
}

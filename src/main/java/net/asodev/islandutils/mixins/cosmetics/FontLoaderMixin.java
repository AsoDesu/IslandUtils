package net.asodev.islandutils.mixins.cosmetics;

import net.asodev.islandutils.modules.cosmetics.CosmeticState;
import net.asodev.islandutils.modules.crafting.CraftingUI;
import net.asodev.islandutils.modules.scavenging.Scavenging;
import net.asodev.islandutils.modules.splits.LevelTimer;
import net.minecraft.client.gui.font.providers.BitmapProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.asodev.islandutils.modules.cosmetics.CosmeticState.MCC_ICONS;

@Mixin(BitmapProvider.Definition.class)
public class FontLoaderMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(ResourceLocation file, int i, int j, int[][] chars, CallbackInfo ci) {
        if (chars.length == 1) {
            int[] c = chars[0];

            StringBuilder builder = new StringBuilder();
            for (int point : c) {
                builder.appendCodePoint(point);
            }

            String character = builder.toString();
            Component comp = Component.literal(character).setStyle(Style.EMPTY.withFont(MCC_ICONS));
            switch (file.getPath()) {
                case "_fonts/body/blueprint_assembly.png" -> CraftingUI.setAssemblerCharacter(character);
                case "_fonts/body/fusion_forge.png" -> CraftingUI.setForgeCharacter(character);
                case "_fonts/body/scavenging.png" -> Scavenging.setTitleCharacter(character);
                case "_fonts/material_dust.png" -> Scavenging.setDustCharacter(character);
                case "_fonts/silver.png" -> Scavenging.setSilverCharacter(character);
                case "_fonts/coin_small.png" -> Scavenging.setCoinCharacter(character);

                case "_fonts/tooltips/hat.png" -> CosmeticState.HAT_COMP = comp;
                case "_fonts/tooltips/accessory.png" -> CosmeticState.ACCESSORY_COMP = comp;
                case "_fonts/tooltips/hair.png" -> CosmeticState.HAIR_COMP = comp;
                case "_fonts/medals.png" -> LevelTimer.medalCharacter = character;
                case "_fonts/split_up.png" -> LevelTimer.splitUpComponent = comp;
                case "_fonts/split_down.png" -> LevelTimer.splitDownComponent = comp;
            }
        }
    }

}

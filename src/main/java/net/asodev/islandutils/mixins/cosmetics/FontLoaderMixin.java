package net.asodev.islandutils.mixins.cosmetics;

import net.asodev.islandutils.state.cosmetics.CosmeticState;
import net.asodev.islandutils.state.faction.FACTION;
import net.asodev.islandutils.state.faction.FactionComponents;
import net.minecraft.client.gui.font.providers.BitmapProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static net.asodev.islandutils.state.cosmetics.CosmeticState.MCC_ICONS;
import static net.asodev.islandutils.state.faction.FactionComponents.MCC_ICONS_19;

@Mixin(BitmapProvider.Definition.class)
public class FontLoaderMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(ResourceLocation file, int i, int j, int[][] chars, CallbackInfo ci) {
        if (chars.length == 1) {
            int[] c = chars[0];

            StringBuilder builder = new StringBuilder();
            for (int point : c) { builder.appendCodePoint(point); }

            Component comp = Component.literal(builder.toString()).setStyle(Style.EMPTY.withFont(MCC_ICONS));
            Component factionComp = Component.literal(builder.toString()).setStyle(Style.EMPTY.withFont(MCC_ICONS_19));
            switch (file.getPath()) {
                case "_fonts/tooltips/hat.png" -> CosmeticState.HAT_COMP = comp;
                case "_fonts/tooltips/accessory.png" -> CosmeticState.ACCESSORY_COMP = comp;
                case "_fonts/tooltips/hair.png" -> CosmeticState.HAIR_COMP = comp;

                case "_fonts/team/aqua_big.png" -> FactionComponents.setComponent(FACTION.AQUA, factionComp);
                case "_fonts/team/blue_big.png" -> FactionComponents.setComponent(FACTION.BLUE, factionComp);
                case "_fonts/team/cyan_big.png" -> FactionComponents.setComponent(FACTION.CYAN, factionComp);
                case "_fonts/team/green_big.png" -> FactionComponents.setComponent(FACTION.GREEN, factionComp);
                case "_fonts/team/lime_big.png" -> FactionComponents.setComponent(FACTION.LIME, factionComp);
                case "_fonts/team/orange_big.png" -> FactionComponents.setComponent(FACTION.ORANGE, factionComp);
                case "_fonts/team/pink_big.png" -> FactionComponents.setComponent(FACTION.PINK, factionComp);
                case "_fonts/team/purple_big.png" -> FactionComponents.setComponent(FACTION.PURPLE, factionComp);
                case "_fonts/team/red_big.png" -> FactionComponents.setComponent(FACTION.RED, factionComp);
                case "_fonts/team/yellow_big.png" -> FactionComponents.setComponent(FACTION.YELLOW, factionComp);
            }
        }
    }

}

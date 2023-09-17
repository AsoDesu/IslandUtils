package net.asodev.islandutils.mixins.cosmetics;

import net.asodev.islandutils.modules.Scavenging;
import net.asodev.islandutils.modules.cosmetics.CosmeticState;
import net.asodev.islandutils.modules.crafting.CraftingUI;
import net.asodev.islandutils.modules.plobby.Plobby;
import net.asodev.islandutils.state.faction.Faction;
import net.asodev.islandutils.state.faction.FactionComponents;
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
import static net.asodev.islandutils.state.faction.FactionComponents.MCC_ICONS_19;

@Mixin(BitmapProvider.Definition.class)
public class FontLoaderMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(ResourceLocation file, int i, int j, int[][] chars, CallbackInfo ci) {
        if (chars.length == 1) {
            int[] c = chars[0];

            StringBuilder builder = new StringBuilder();
            for (int point : c) { builder.appendCodePoint(point); }

            String character = builder.toString();
            Component comp = Component.literal(character).setStyle(Style.EMPTY.withFont(MCC_ICONS));
            Component factionComp = Component.literal(character).setStyle(Style.EMPTY.withFont(MCC_ICONS_19));
            switch (file.getPath()) {
                case "_fonts/framework_assembler.png" -> CraftingUI.setAssemblerCharacter(character);
                case "_fonts/header/fusion_crafting_tab_1.png" -> CraftingUI.setForgeCharacter(character);
                case "_fonts/header/scavenging.png" -> Scavenging.setTitleCharacter(character);
                case "_fonts/header/plobby_tab1.png" -> Plobby.setTitleCharacter(character);
                case "_fonts/silver.png" -> Scavenging.setSilverCharacter(character);

                case "_fonts/tooltips/hat.png" -> CosmeticState.HAT_COMP = comp;
                case "_fonts/tooltips/accessory.png" -> CosmeticState.ACCESSORY_COMP = comp;
                case "_fonts/tooltips/hair.png" -> CosmeticState.HAIR_COMP = comp;
                case "_fonts/medals.png" -> LevelTimer.medalCharacter = character;
                case "_fonts/split_up.png" -> LevelTimer.splitUpComponent = comp;
                case "_fonts/split_down.png" -> LevelTimer.splitDownComponent = comp;

                case "_fonts/team/aqua_big.png" -> FactionComponents.setComponent(Faction.AQUA, factionComp);
                case "_fonts/team/blue_big.png" -> FactionComponents.setComponent(Faction.BLUE, factionComp);
                case "_fonts/team/cyan_big.png" -> FactionComponents.setComponent(Faction.CYAN, factionComp);
                case "_fonts/team/green_big.png" -> FactionComponents.setComponent(Faction.GREEN, factionComp);
                case "_fonts/team/lime_big.png" -> FactionComponents.setComponent(Faction.LIME, factionComp);
                case "_fonts/team/orange_big.png" -> FactionComponents.setComponent(Faction.ORANGE, factionComp);
                case "_fonts/team/pink_big.png" -> FactionComponents.setComponent(Faction.PINK, factionComp);
                case "_fonts/team/purple_big.png" -> FactionComponents.setComponent(Faction.PURPLE, factionComp);
                case "_fonts/team/red_big.png" -> FactionComponents.setComponent(Faction.RED, factionComp);
                case "_fonts/team/yellow_big.png" -> FactionComponents.setComponent(Faction.YELLOW, factionComp);
            }
        }
    }

}

package net.asodev.islandutils.mixins.cosmetics;

import net.asodev.islandutils.modules.cosmetics.CosmeticState;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.util.ChatUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.function.Consumer;

import static net.asodev.islandutils.util.ChatUtils.iconsFontStyle;

@Mixin(ItemStack.class)
public class PreviewTutorialMixin {
    private static final Style style0 = Style.EMPTY.withColor(ChatFormatting.DARK_GRAY).withFont(Style.DEFAULT_FONT);
    private static final Style style1 = Style.EMPTY.withColor(ChatUtils.parseColor("#e9d282")).withFont(Style.DEFAULT_FONT);
    private static final Style style2 = Style.EMPTY.withColor(ChatUtils.parseColor("#fbe460")).withFont(Style.DEFAULT_FONT);

    private static final Component previewComponent = Component.empty()
            .append(Component.literal("\ue005").setStyle(iconsFontStyle))
            .append(Component.literal(" > ").setStyle(style0))
            .append(Component.keybind("key.pickItem").append(" to ").setStyle(style1))
            .append(Component.literal("Preview on player").setStyle(style2));

    @Inject(
            method = "getTooltipLines",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;addAttributeTooltips(Ljava/util/function/Consumer;Lnet/minecraft/world/entity/player/Player;)V", shift = At.Shift.BEFORE),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private void injectedTooltipLines(Item.TooltipContext tooltipContext, @Nullable Player player, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir, List<Component> list, MutableComponent mutableComponent, Consumer consumer) {
        if (CosmeticState.getType((ItemStack)(Object)this) == null) return;
        if (!IslandOptions.getCosmetics().isShowPlayerPreview()) return;
        list.add(previewComponent);
    }

}

package net.asodev.islandutils.mixins.cosmetics;

import com.llamalad7.mixinextras.sugar.Local;
import net.asodev.islandutils.modules.cosmetics.CosmeticState;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.util.ChatUtils;
import net.asodev.islandutils.util.FontUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public class PreviewTutorialMixin {
    @Unique
    private static final Style style0 = Style.EMPTY.withColor(ChatFormatting.DARK_GRAY).withFont(Style.DEFAULT_FONT);
    @Unique
    private static final Style style1 = Style.EMPTY.withColor(ChatUtils.parseColor("#e9d282")).withFont(Style.DEFAULT_FONT);
    @Unique
    private static final Style style2 = Style.EMPTY.withColor(ChatUtils.parseColor("#fbe460")).withFont(Style.DEFAULT_FONT);

    @Unique
    private static final Component previewComponent = Component.empty()
            .append(FontUtils.ICON_MIDDLE_CLICK)
            .append(Component.literal(" > ").setStyle(style0))
            .append(Component.keybind("key.pickItem").append(" to ").setStyle(style1))
            .append(Component.literal("Preview on player").setStyle(style2));

    @Inject(
            method = "addDetailsToTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;addAttributeTooltips(Ljava/util/function/Consumer;Lnet/minecraft/world/item/component/TooltipDisplay;Lnet/minecraft/world/entity/player/Player;)V",
                    shift = At.Shift.BEFORE
            )
    )
    private void injectedTooltipLines(Item.TooltipContext tooltipContext, TooltipDisplay tooltipDisplay, @Nullable Player player, TooltipFlag tooltipFlag, Consumer<Component> consumer, CallbackInfo ci) {
        if (CosmeticState.getType((ItemStack) (Object) this) == null) return;
        if (!IslandOptions.getCosmetics().isShowPlayerPreview()) return;
        consumer.accept(previewComponent);
    }

}

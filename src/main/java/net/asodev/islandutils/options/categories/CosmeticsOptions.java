package net.asodev.islandutils.options.categories;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.network.chat.Component;

public class CosmeticsOptions implements OptionsCategory {
    private static final CosmeticsOptions defaults = new CosmeticsOptions();

    boolean showPlayerPreview = true;
    boolean showOnHover = true;
    boolean showOnOnlyCosmeticMenus = true;

    public boolean isShowPlayerPreview() {
        return showPlayerPreview;
    }

    public boolean isShowOnHover() {
        return showOnHover;
    }

    public boolean isShowOnOnlyCosmeticMenus() {
        return showOnOnlyCosmeticMenus;
    }

    @Override
    public ConfigCategory getCategory() {
        Option<Boolean> showPreviewOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.showPlayerPreview"))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.showPlayerPreview, () -> showPlayerPreview, value -> this.showPlayerPreview = value)
                .build();
        Option<Boolean> showHoverOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.showOnHover"))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.showOnHover, () -> showOnHover, value -> this.showOnHover = value)
                .build();
        Option<Boolean> showInOnlyCosmeticMenu = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.showOnOnlyCosmeticMenus"))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.showOnOnlyCosmeticMenus, () -> showOnOnlyCosmeticMenus, value -> this.showOnOnlyCosmeticMenus = value)
                .build();
        return ConfigCategory.createBuilder()
                .name(Component.literal("Cosmetics"))
                .option(showPreviewOption)
                .option(showHoverOption)
                .option(showInOnlyCosmeticMenu)
                .build();
    }
}

package net.asodev.islandutils.options.categories;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.asodev.islandutils.options.saving.Ignore;
import net.minecraft.network.chat.Component;

public class CosmeticsOptions implements OptionsCategory {
    @Ignore
    private static final CosmeticsOptions defaults = new CosmeticsOptions();

    boolean showPlayerPreview = true;
    boolean showOnHover = true;
    boolean showOnOnlyCosmeticMenus = true;
    boolean showReputationBar = true;

    public boolean isShowPlayerPreview() {
        return showPlayerPreview;
    }

    public boolean isShowOnHover() {
        return showOnHover;
    }

    public boolean isShowOnOnlyCosmeticMenus() {
        return showOnOnlyCosmeticMenus;
    }

    public boolean isShowReputationBar() {
        return showReputationBar;
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
        Option<Boolean> showOnlyCosmeticMenusOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.showOnOnlyCosmeticMenus"))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.showOnOnlyCosmeticMenus, () -> showOnOnlyCosmeticMenus, value -> this.showOnOnlyCosmeticMenus = value)
                .build();
        Option<Boolean> showReputationBarOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.showReputationBar"))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.showReputationBar, () -> showReputationBar, value -> this.showReputationBar = value)
                .build();
        return ConfigCategory.createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.category.cosmetics"))
                .option(showPreviewOption)
                .option(showHoverOption)
                .option(showOnlyCosmeticMenusOption)
                .option(showReputationBarOption)
                .build();
    }
}

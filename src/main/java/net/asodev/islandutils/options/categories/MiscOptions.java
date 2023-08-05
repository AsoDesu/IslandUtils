package net.asodev.islandutils.options.categories;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.asodev.islandutils.options.saving.Ignore;
import net.minecraft.network.chat.Component;

public class MiscOptions implements OptionsCategory {
    @Ignore
    private static final MiscOptions defaults = new MiscOptions();

    boolean pauseConfirm = true;
    boolean showFriendsInGame = true;
    boolean silverPreview = true;
    boolean enableConfigButton = true;
    boolean debugMode = false;
    boolean debugActivityUi = false;

    public boolean isPauseConfirm() {
        return pauseConfirm;
    }
    public boolean isShowFriendsInGame() {
        return showFriendsInGame;
    }
    public boolean isSilverPreview() {
        return silverPreview;
    }
    public boolean isEnableConfigButton() {
        return enableConfigButton;
    }
    public boolean isDebugMode() {
        return debugMode;
    }

    public boolean isDebugActivityUi() {
        return debugActivityUi;
    }

    @Override
    public ConfigCategory getCategory() {
        Option<Boolean> pauseOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.pauseConfirm"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.pauseConfirm.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.pauseConfirm, () -> pauseConfirm, value -> this.pauseConfirm = value)
                .build();
        Option<Boolean> showFriendsOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.showFriendsInGame"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.showFriendsInGame.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.showFriendsInGame, () -> showFriendsInGame, value -> this.showFriendsInGame = value)
                .build();
        Option<Boolean> silverOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.silverPreview"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.silverPreview.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.silverPreview, () -> silverPreview, value -> this.silverPreview = value)
                .build();
        Option<Boolean> buttonOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.enableConfigButton"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.enableConfigButton.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.enableConfigButton, () -> enableConfigButton, value -> this.enableConfigButton = value)
                .build();
        Option<Boolean> debugOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.debugMode"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.debugMode.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.debugMode, () -> debugMode, value -> this.debugMode = value)
                .build();
        Option<Boolean> debugActivityUiOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.debugActivityUi"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.debugActivityUi.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.debugActivityUi, () -> debugActivityUi, value -> this.debugActivityUi = value)
                .build();

        return ConfigCategory.createBuilder()
                .name(Component.literal("Miscellaneous"))
                .option(pauseOption)
                .option(showFriendsOption)
                .option(silverOption)
                .option(buttonOption)
                .group(OptionGroup.createBuilder()
                        .name(Component.literal("Debug Options"))
                        .collapsed(true)
                        .option(debugOption)
                        .option(debugActivityUiOption)
                        .build())
                .build();
    }
}

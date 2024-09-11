package net.asodev.islandutils.options.categories;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.asodev.islandutils.options.saving.Ignore;
import net.asodev.islandutils.util.Utils;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;

public class MiscOptions implements OptionsCategory {
    @Ignore
    private static final MiscOptions defaults = new MiscOptions();

    boolean pauseConfirm = true;
    boolean showFriendsInGame = true;
    boolean showFriendsInLobby = true;
    boolean silverPreview = true;
    boolean channelSwitchers = true;
    boolean enableConfigButton = true;
    boolean iconsButton = true;
    boolean debugMode = false;

    public boolean isPauseConfirm() {
        return pauseConfirm;
    }
    public boolean isShowFriendsInGame() {
        return showFriendsInGame;
    }
    public boolean isShowFriendsInLobby() {
        return showFriendsInLobby;
    }

    public boolean isSilverPreview() {
        return silverPreview;
    }
    public boolean isEnableConfigButton() {
        return enableConfigButton;
    }
    public boolean useGameIcons() {
        return iconsButton;
    }
    public boolean showChannelSwitchers() {
        return channelSwitchers;
    }
    public boolean isDebugMode() {
        return debugMode && !Utils.isLunarClient();
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
        Option<Boolean> showFriendsLobbyOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.showFriendsInLobby"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.showFriendsInLobby.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.showFriendsInLobby, () -> showFriendsInLobby, value -> this.showFriendsInLobby = value)
                .build();
        Option<Boolean> silverOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.silverPreview"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.silverPreview.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.silverPreview, () -> silverPreview, value -> this.silverPreview = value)
                .build();
        Option<Boolean> channelsOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.channelSwitchers"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.channelSwitchers.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.channelSwitchers, () -> channelSwitchers, value -> this.channelSwitchers = value)
                .build();
        Option<Boolean> buttonOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.enableConfigButton"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.enableConfigButton.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.enableConfigButton, () -> enableConfigButton, value -> this.enableConfigButton = value)
                .build();
        Option<Boolean> iconsOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.iconsButton"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.iconsButton.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.iconsButton, () -> iconsButton, value -> this.iconsButton = value)
                .build();
        Option<Boolean> debugOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.debugMode"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.debugMode.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .available(!Utils.isLunarClient()) // disable on lunar client
                .binding(defaults.debugMode, () -> debugMode, value -> this.debugMode = value)
                .build();

        return ConfigCategory.createBuilder()
                .name(Component.literal("Miscellaneous"))
                .option(pauseOption)
                .group(OptionGroup.createBuilder()
                        .name(Component.literal("Friends Notifier"))
                        .collapsed(false)
                        .option(showFriendsOption)
                        .option(showFriendsLobbyOption)
                        .build())
                .option(silverOption)
                .option(channelsOption)
                .option(buttonOption)
                .group(OptionGroup.createBuilder()
                        .name(Component.literal("Icons"))
                        .collapsed(false)
                        .option(iconsOption)
                        .build())
                .group(OptionGroup.createBuilder()
                        .name(Component.literal("Debug Options"))
                        .collapsed(true)
                        .option(debugOption)
                        .build())
                .build();
    }
}
